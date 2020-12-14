import torch
from torch import nn
import torchaudio

class TextTransform:
    """
    Class to map characters to integers and vice versa
    """
    def __init__(self):
        char_map_str = """
                       ' 0
                       <SPACE> 1
                       a 2
                       b 3
                       c 4
                       d 5
                       e 6
                       f 7
                       g 8
                       h 9
                       i 10
                       j 11
                       k 12
                       l 13
                       m 14
                       n 15
                       o 16
                       p 17
                       q 18
                       r 19
                       s 20
                       t 21
                       u 22
                       v 23
                       w 24
                       x 25
                       y 26
                       z 27
                       """
        self.char_map = {}
        self.index_map = {}
        for line in char_map_str.strip().split('\n'):
            ch, index = line.split()
            self.char_map[ch] = int(index)
            self.index_map[int(index)] = ch
        self.index_map[1] = ' '

    def text_to_int(self, text):
        """
        Utility function to map text to an integer sequence
        """
        int_sequence = []
        for c in text:
            if c == ' ':
                ch = self.char_map['<SPACE>']
            else:
                ch = self.char_map[c]
            int_sequence.append(ch)
        return int_sequence

    def int_to_text(self, labels):
        """ 
        Utility function to map text to an integer sequence
        """
        string = []
        for i in labels:
            string.append(self.index_map[i])
        return ''.join(string).replace('<SPACE>', ' ')


def data_processing(data, data_type="train"):
    """
    Utility function to apply transforms to train and validation datasets
    """
    
    text_transform = TextTransform()
    
    spectrograms = []
    labels = []
    input_lengths = []
    label_lengths = []
    
    for (waveform, _, utterance, _, _, _) in data:
        if data_type == 'train':
            
            # Transforms to be applied on train data
            train_audio_transforms = nn.Sequential(torchaudio.transforms.MelSpectrogram(sample_rate=16000, n_mels=128),
                                                   torchaudio.transforms.FrequencyMasking(freq_mask_param=15),
                                                   torchaudio.transforms.TimeMasking(time_mask_param=35))
            
            spec = train_audio_transforms(waveform).squeeze(0).transpose(0, 1)
        
        elif data_type == 'valid':
            
            # Transforms to be applied on validation data
            valid_audio_transforms = torchaudio.transforms.MelSpectrogram()

            spec = valid_audio_transforms(waveform).squeeze(0).transpose(0, 1)
        else:
            raise Exception('data_type should be train or valid')
            
        spectrograms.append(spec)
        label = torch.Tensor(text_transform.text_to_int(utterance.lower()))
        labels.append(label)
        input_lengths.append(spec.shape[0]//2)
        label_lengths.append(len(label))

    spectrograms = nn.utils.rnn.pad_sequence(spectrograms, batch_first=True).unsqueeze(1).transpose(2, 3)
    labels = nn.utils.rnn.pad_sequence(labels, batch_first=True)

    return spectrograms, labels, input_lengths, label_lengths