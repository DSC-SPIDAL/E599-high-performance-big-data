# ~/anaconda3/bin/python
"""
Created by: Sidharth Bhakth
Created on: 24th November 2020
"""

import warnings
warnings.filterwarnings("ignore")

import time
import os
import argparse
import torch
from torch import nn
import torch.utils.data as data
import torch.optim as optim
import torch.nn.functional as F
import torchaudio
import torch.distributed as dist
from torch.utils.data.distributed import DistributedSampler
from torch.nn.parallel import DistributedDataParallel
import numpy as np
import pandas as pd
from preprocessing import TextTransform, data_processing
from evaluation import levenshtein_distance, word_errors, char_errors, wer, cer
from DeepSpeech2 import CNNLayerNorm, CNN, BidirectionalRNN, DeepSpeech2
from ImprovedDeepSpeech2 import ResidualCNN, BidirectionalGRU, ImprovedDeepSpeech2
from training import train, GreedyDecoder, test

# Parse variables
parser = argparse.ArgumentParser(formatter_class=argparse.ArgumentDefaultsHelpFormatter)
parser.add_argument('--model', type = str, default = 'baseline')
parser.add_argument('--train_url', type = str, default = 'dev-clean')
parser.add_argument('--use_cuda', type = bool, default = True)
parser.add_argument('--seed', type = int, default = 1)
parser.add_argument('--backend', type = str, default = 'nccl')
parser.add_argument('--local_rank', type = int)
args = parser.parse_args()

def main():
    
    num_gpus = torch.cuda.device_count()
    
    dist.init_process_group(backend = args.backend, init_method = "env://")

    # Hyperparameters
    hparams = {"n_cnn_layers": 3,
               "n_rnn_layers": 5,
               "rnn_dim": 512,
               "n_class": 29,
               "n_feats": 128,
               "stride": 2,
               "dropout": 0.1,
               "learning_rate": 5e-4,
               "batch_size": 10,
               "epochs": 20}
    
    # Load train and test data
    train_dataset = torchaudio.datasets.LIBRISPEECH("./data", url = args.train_url, download = False)
    train_sampler = DistributedSampler(dataset = train_dataset)
    train_loader = data.DataLoader(dataset = train_dataset,
                                   batch_size = hparams['batch_size'],
                                   sampler = train_sampler,
                                   collate_fn = lambda x : data_processing(x, 'train'),
                                   num_workers = num_gpus,
                                   pin_memory = True)
    
    test_dataset = torchaudio.datasets.LIBRISPEECH("./data", url = "test-clean", download = False)
    test_loader = data.DataLoader(dataset = test_dataset,
                                  batch_size = hparams['batch_size'],
                                  shuffle = False,
                                  collate_fn = lambda x : data_processing(x, 'valid'),
                                  num_workers = num_gpus,
                                  pin_memory = True)
    
    # Set seed for reproducibility across all GPUs
    torch.manual_seed(args.seed)
    torch.backends.cudnn.deterministic = True
    torch.backends.cudnn.benchmark = False
    torch.backends.cudnn.enabled = False
    
    # Instantiate model
    if args.model == "baseline":
        model = DeepSpeech2(hparams['n_cnn_layers'],
                            hparams['n_rnn_layers'],
                            hparams['rnn_dim'],
                            hparams['n_class'],
                            hparams['n_feats'],
                            hparams['stride'],
                            hparams['dropout'])

    elif args.model == "improved":
        model = ImprovedDeepSpeech2(hparams['n_cnn_layers'],
                                    hparams['n_rnn_layers'],
                                    hparams['rnn_dim'],
                                    hparams['n_class'],
                                    hparams['n_feats'],
                                    hparams['stride'],
                                    hparams['dropout'])
    
    
    device = torch.device('cuda:{}'.format(args.local_rank))
    model.to(device)
    model = DistributedDataParallel(model, device_ids = [args.local_rank], output_device = args.local_rank)

    optimizer = optim.AdamW(model.parameters(), hparams['learning_rate'])
    criterion = nn.CTCLoss(blank=28)
    scheduler = optim.lr_scheduler.OneCycleLR(optimizer,
                                              max_lr = hparams['learning_rate'], 
                                              steps_per_epoch = int(len(train_loader)),
                                              epochs = hparams['epochs'],
                                              anneal_strategy = 'linear')

    results = []
    
    start = time.perf_counter()
    
    for epoch in range(1, hparams['epochs'] + 1):
        print(f"Local Rank: {args.local_rank}, Epoch: {epoch}, Training...")
        train(model, device, train_loader, criterion, optimizer, scheduler, epoch)
              
        if args.local_rank == 0:
            avg_cer, avg_wer = test(model, device, test_loader, criterion, epoch)
            print(f"Epoch: {epoch:2} Average CER: {avg_cer:.4f}, Average WER: {avg_wer:.4f}\n")
            results.append([avg_cer, avg_wer])
    
    end = time.perf_counter()
    print(f"Execution time: {end - start:0.2f} seconds")
    
    df = pd.DataFrame(results, columns = ['avg_cer', 'avg_wer'])
    df.to_csv(args.model + '_' + args.train_url + '.csv', index = False)

if __name__ == '__main__':
    main()