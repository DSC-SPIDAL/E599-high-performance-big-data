import pandas as pd
import numpy as np
import csv
import matplotlib.pyplot as plt
import time

from sklearn.preprocessing import MinMaxScaler

import torch
import torch.nn as nn
import torch.optim as optim
import torch.nn.functional as F
from torch.utils.data import TensorDataset, DataLoader

from torch.utils.data.distributed import DistributedSampler

import argparse
import os
import random

def set_random_seeds(random_seed=0):
    torch.manual_seed(random_seed)
    #torch.backends.cudnn.deterministic = True
    torch.set_deterministic(True)
    #torch.backends.cudnn.benchmark = False
    np.random.seed(random_seed)
    random.seed(random_seed)

def create_dataset(X, scaler, time_steps=1):
    # reshape to [samples, time_steps, n_features]
    Xs, ys = [], []

    def single_col_dataset(X):
        for i in range(len(X) - time_steps):
            v = X[i:(i + time_steps)]
            Xs.append(scaler.transform(np.expand_dims(v,1)))
            ys.append(scaler.transform(X[i+time_steps].reshape(1,1)))
            #ys.append(scaler.transform(np.expand_dims(X[i + 1: (i+ 1 + time_steps)],1)))

    if type(X) is pd.DataFrame:
        for col_name in (X.columns.values):
            car_data = X[col_name].values
            single_col_dataset(car_data)
    # if there is single column, we don't need two loops.
    else:
        single_col_dataset(X)

    return np.array(Xs), np.array(ys)[:,:,0]

class LSTMNet(nn.Module):
    def __init__(self, input_dim, hidden_dim, seq_len, batch_size, device, num_layers=2):
        super(LSTMNet, self).__init__()

        self.input_dim = input_dim
        self.hidden_dim = hidden_dim
        self.seq_len = seq_len
        self.num_layers = num_layers
        self.batch_size = batch_size
        self.device = device

        self.lstm = nn.LSTM(input_size=input_dim, 
                            hidden_size=hidden_dim, 
                            num_layers=num_layers, 
                            dropout=0.5,
                            batch_first=True)
        self.linear = nn.Linear(in_features=hidden_dim, 
                                out_features=1) 

    def reset_hidden_state(self, batch_size):
        self.hidden = (
            torch.zeros(self.num_layers, batch_size, self.hidden_dim),
            torch.zeros(self.num_layers, batch_size, self.hidden_dim)
        )   
        self.hidden = (self.hidden[0].to(self.device),
                       self.hidden[1].to(self.device))

    def forward(self, input):
        self.reset_hidden_state(len(input))
        lstm_out, self.hidden = self.lstm(input, self.hidden)
        out = lstm_out.squeeze()[:,-1,:]
        y_pred = self.linear(out) 

        return y_pred

def train_model(model, train_loader, val_loader, batch_size, device, lr, num_epochs, local_rank, model_filepath):
    loss_fn = torch.nn.MSELoss(reduction='mean') #nn.L1Loss()
    optimizer = optim.Adam(model.parameters(), lr=lr)

    num_epochs = num_epochs

    train_hist = np.zeros(num_epochs)
    val_hist = np.zeros(num_epochs)

    valid_loss_min = np.Inf

    for t in range(num_epochs):
        print("Local Rank: {}, Epoch: {}, Training ...".format(local_rank, t))
        #print("Training Epoch: ", t)
        train_loss = []
        for input, labels in train_loader:
            model.zero_grad()
            input, labels = input.to(device), labels.to(device)
            y_pred = model(input)

            loss = loss_fn(y_pred.float(), labels)
            train_loss.append(loss.item())

            optimizer.zero_grad()
            loss.backward()
            optimizer.step()


        val_loss = []
        model.eval()
        for v_input, v_labels in val_loader:
            v_input, v_labels = v_input.to(device), v_labels.to(device)
            pred = model(v_input)
            v_loss = loss_fn(pred.float(), v_labels)
            val_loss.append(v_loss.item())
        model.train()

        train_hist[t] = np.mean(train_loss)
        print(f'Epoch {t} train loss: {train_hist[t]}')
        val_hist[t] = np.mean(val_loss)
        print(f'Epoch {t} val loss: {val_hist[t]}')

        if np.mean(val_loss) <= valid_loss_min:
            torch.save(model.state_dict(), model_filepath)
            print('Validation loss decreased ({:.6f} --> {:.6f}).  Saving model ...'.format(valid_loss_min,np.mean(val_loss)))
            valid_loss_min = np.mean(val_loss)

    return model.eval(), train_hist, val_hist

def evaluate(model, val_loader, test_loader, device):
    # Calculate mse and rmse for val dataset
    prediction = None
    r_val = []
    m_val = []
    with torch.no_grad():
        for x, y in val_loader:
            x, y = x.to(device), y.to(device)
            prediction = model(x)
            rmse = torch.sqrt(torch.sum((prediction - y)**2)/len(prediction))
            mse = torch.sum((prediction - y)**2)/len(prediction)
            r_val.append(rmse.item())
            m_val.append(mse.item())
    #print("Val RMSE and MSE: ", np.mean(r), np.mean(m))

    # Calculate mse and rmse for test dataset
    prediction = None
    r_test = []
    m_test = []
    with torch.no_grad():
        for x, y in test_loader:
            x, y = x.to(device), y.to(device)
            prediction = model(x)
            rmse = torch.sqrt(torch.sum((prediction - y)**2)/len(prediction))
            mse = torch.sum((prediction - y)**2)/len(prediction)
            r_test.append(rmse.item())
            m_test.append(mse.item())
    #print("Test RMSE and MSE: ", np.mean(r), np.mean(m))

    return np.mean(m_val), np.mean(r_val), np.mean(m_test), np.mean(r_test)

def main():

    num_epochs_default = 50 # 10000
    batch_size_default = 512 # 256 # 1024
    learning_rate_default = 0.001 # 0.1
    random_seed_default = 0
    model_dir_default = "saved_models"
    model_filename_default = "checkpoint_distributed.pt"
    data_filename_default = "indycar_data.csv"
    time_step_default = 80
    train_default = True
    car_anomaly_default = 'car_12'

    # For Distributed Training
    # Each process runs on 1 GPU device specified by the local_rank argument.
    parser = argparse.ArgumentParser(formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser.add_argument("--local_rank", type=int, help="Local rank. Necessary for using the torch.distributed.launch utility.")
    parser.add_argument("--num_epochs", type=int, help="Number of training epochs.", default=num_epochs_default)
    parser.add_argument("--batch_size", type=int, help="Training batch size for one process.", default=batch_size_default)
    parser.add_argument("--learning_rate", type=float, help="Learning rate.", default=learning_rate_default)
    parser.add_argument("--random_seed", type=int, help="Random seed.", default=random_seed_default)
    parser.add_argument("--model_dir", type=str, help="Directory for saving models.", default=model_dir_default)
    parser.add_argument("--model_filename", type=str, help="Model filename.", default=model_filename_default)
    parser.add_argument("--resume", action="store_true", help="Resume training from saved checkpoint.")
    parser.add_argument("--time_step", type=int, help="Time step or sequence length.", default=time_step_default)
    parser.add_argument("--train", type=bool, help="True for training. False for Inference", default=train_default)
    parser.add_argument("--data_filename", type=str, help="Data CSV filename.", default=data_filename_default)
    parser.add_argument("--car", type=str, help="Car number for anomaly.", default=car_anomaly_default)
    argv = parser.parse_args()

    local_rank = argv.local_rank
    num_epochs = argv.num_epochs
    batch_size = argv.batch_size
    learning_rate = argv.learning_rate
    random_seed = argv.random_seed
    model_dir = argv.model_dir
    model_filename = argv.model_filename
    resume = argv.resume
    time_step = argv.time_step
    data_filename = argv.data_filename
    train = argv.train
    car_anomaly = argv.car

    model_filepath = os.path.join(model_dir, model_filename)

    # We need to use seeds to make sure that the models initialized in different processes are the same
    set_random_seeds(random_seed=random_seed)

    # Initializes the distributed backend which will take care of sychronizing nodes/GPUs
    # nccl for GPUs. gloo for CPUs.
    # torch.distributed.init_process_group(backend="nccl")
    torch.distributed.init_process_group(backend="gloo")


    #csv_file_name = 'indycar_sample_data.csv'
    dataset = pd.read_csv(data_filename)
    #dataset = dataset.drop(['Unnamed: 0','index'], axis=1)

    comp_cars = ['car_12', 'car_20', 'car_9', 'car_27', 'car_28', 'car_22', 'car_29',
             'car_1', 'car_6', 'car_15', 'car_66', 'car_98', 'car_4', 'car_88',
             'car_25', 'car_60', 'car_64', 'car_23', 'car_19', 'car_21', 'car_17',
             'car_59', 'car_26', 'car_7']

    dataset = dataset[comp_cars]

    scaler = MinMaxScaler(feature_range=(0, 1))
    scaler = scaler.fit(np.expand_dims(dataset.car_12.values, 1))

    train_size = int(len(dataset) * 0.4)
    val_size = int((len(dataset) * 0.1) + train_size)
    test_size = len(dataset) - train_size - val_size
    
    #time_step = 80

    train_X, train_y = create_dataset(dataset.iloc[0:train_size], scaler, time_step)
    val_X, val_y = create_dataset(dataset.iloc[train_size:val_size], scaler, time_step)
    test_X, test_y = create_dataset(dataset.iloc[val_size:len(dataset)], scaler, time_step)

    #device = torch.device('cuda:{}'.format(local_rank) if torch.cuda.is_available() else 'cpu')
    device = torch.device('cpu')

    train_data = TensorDataset(torch.from_numpy(train_X).float(), torch.from_numpy(train_y).float())
    val_data = TensorDataset(torch.from_numpy(val_X).float(), torch.from_numpy(val_y).float())
    test_data = TensorDataset(torch.from_numpy(test_X).float(), torch.from_numpy(test_y).float())

    # Restricts data loading to a subset of the dataset exclusive to the current process
    train_sampler = DistributedSampler(dataset=train_data)
    #val_sampler = DistributedSampler(dataset=val_data)
    #test_sampler = DistributedSampler(dataset=test_data)

    #batch_size = 512

    train_loader = DataLoader(train_data, shuffle=False, batch_size=batch_size, sampler=train_sampler, num_workers=0)
    val_loader = DataLoader(val_data, shuffle=False, batch_size=batch_size, num_workers=0)
    test_loader = DataLoader(test_data, shuffle=False, batch_size=batch_size, num_workers=0)

    model = LSTMNet(1, 128, seq_len=time_step, batch_size=batch_size, device=device, num_layers=2)

    #device = torch.device("cuda:{}".format(local_rank))
    #device = torch.device("cpu")

    model.to(device)

    # For GPU
    #ddp_model = torch.nn.parallel.DistributedDataParallel(model, device_ids=[local_rank], output_device=local_rank)
    
    # For CPU
    ddp_model = torch.nn.parallel.DistributedDataParallel(model)

    # We only save the model who uses device "cuda:0"
    # To resume, the device for the saved model would also be "cuda:0"
    map_location = {"cpu":"cpu"}
    if resume == True:
        #map_location = {"cuda:0": "cuda:{}".format(local_rank)}
        #map_location = {"cpu":"cpu"}
        ddp_model.load_state_dict(torch.load(model_filepath, map_location=map_location))


    if train is True:
        start_time = time.time()
        model, train_hist, val_hist = train_model(ddp_model, train_loader, val_loader, batch_size, device, learning_rate, num_epochs, local_rank, model_filepath)
        end_time = time.time()

        print(f"Total Training Time: {end_time-start_time} seconds")

        plt.figure(figsize=[10,5])
        plt.plot(train_hist)
        plt.plot(val_hist)
        plt.legend(['Training Loss', 'Validation Loss'])
        #plt.show()
        plt.savefig(f"loss_multi_node_{device}.png")

    else:
        ddp_model.load_state_dict(torch.load(model_filepath, map_location=map_location))


    #print("Train History: ", train_hist)
    #print("Val History: ", val_hist)

    mse_val, rmse_val, mse_test, rmse_test = evaluate(ddp_model, val_loader, test_loader, device)
    print(f"Validation Set MSE: {mse_val} RMSE: {rmse_val}")
    print(f"Test Set MSE: {mse_test} RMSE: {rmse_test}")
    
    # Anomaly detection

    winner_X, winner_y = create_dataset(dataset[car_anomaly], scaler, time_steps=time_step)

    winner_X = torch.from_numpy(winner_X).float().to(device)
    winner_y = torch.from_numpy(winner_y).float().to(device)

    #if train == False:
    #    model.load_state_dict(torch.load(model_filepath, map_location=device))

    prediction = None
    with torch.no_grad():
        #model.reset_hidden_state(len(winner_X))
        prediction = ddp_model(winner_X)
        #prediction = prediction.float()
    prediction = prediction.cpu()
    winner_y = winner_y.cpu()

    plt.figure(figsize=[10, 5])
    plt.plot(winner_y)
    plt.plot(prediction)
    plt.legend(['True', 'Predicted'])
    #plt.show()
    plt.savefig(f"true_pred_multi_node_{time_step}_{device}.png")

    plt.figure(figsize=[10,5])
    plt.plot(scaler.inverse_transform(winner_y))
    plt.plot(scaler.inverse_transform(prediction))
    plt.legend(['True', 'Predicted'])
    #plt.show()
    plt.savefig(f"true_pred_inverse_multi_node_{time_step}_{device}.png")

    loss = np.abs(prediction[:,0] - winner_y[:,0])

    anomaly_result = np.zeros(len(loss))
    threshold = 0.045

    anomaly_result[loss > threshold] = 1

    plt.figure(figsize=[10,5])
    plt.plot(anomaly_result)
    #plt.show()
    plt.savefig(f"anomaly_multi_node_{time_step}_{device}.png")

    fig, ax = plt.subplots()
    fig.set_size_inches(25, 10)
    plt.plot(scaler.inverse_transform(prediction))
    ax.pcolorfast(ax.get_xlim(), ax.get_ylim(),
                anomaly_result[np.newaxis],
                cmap='Reds', alpha=0.5)
    plt.savefig(f"anomaly_speed_multi_node_{time_step}_{device}.png")


if __name__ == "__main__":
    main()



