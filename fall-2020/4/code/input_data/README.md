The dataset we are using is the LibriSpeech corpus which is derived from audiobooks that are part of the open source LibriVox project and contains 1000 hours of speech sampled at 16 kHz.<sup>[1]</sup> The data consists of automatically aligned and segmented English read speech from audiobooks with the corresponding book text and is suitable for training speech recognition systems. The corpus is freely available and is easy to access using the torchaudio package in the PyTorch library. We have used the 360 hours long train dataset which is about 23 GB in size. The data has 110K audio samples and training can only be done in small minibatches as the entire dataset is very large to fit into the memory of a GPU. A snapshot of the data is shown below:

![data](https://github.com/SidharthBhakth/E599-high-performance-big-data/blob/master/fall-2020/4/images/data.png)

Each sample of the dataset contains the waveform, sample rate of audio, the utterance/label, and more metadata on the sample. 

[1] LibriSpeech ASR Corpus: http://www.openslr.org/12
