# Deep Learning based Speech-to-Text Framework

**Created by:** Sidharth Vishnu Bhakth, Vijayalaxmi B Maigur

### Summary:

Deep Neural Networks and end-to-end models have significantly changed the landscape of speech recognition and language modeling. They have eliminated the need of hand-engineering features and allow the model to learn feature representations from clean/noisy speech signals. With large enough datasets, these networks can achieve state-of-the-art performance in speech recognition tasks and have offered significant improvement over the traditional approach of hand-engineering of features which are commonly used in automatic speech recognition (ASR) systems. They are commonly used in hybrid DNN-HMM speech recognition systems for acoustic modeling, pronunciation models that map words to phonemes and language models that map speech to text. One of the biggest challenges in speech recognition is that it contains a wide range of variability in speech and acoustics. Thus, a good speech recognition model should be able to generalize and perform robustly even with varied acoustics. In traditional automatic speech recognition (ASR) systems, numerous complex components like feature extraction, pronunciation models, speaker adaption, acoustic models etc. are modelled separately. Building and tuning these individual models are very hard especially for a new language.

Recent work in this domain attempts to unify the components into a single end-to-end pipeline capable of taking in audio input and directly output text transcriptions. Two of the most popular end-to-end models in use today are Deep Speech by Baidu and Listen Attend and Spell (LAS) by Google. Both Deep Speech and LAS, are recurrent neural network (RNN) based architectures with different approaches to modeling speech recognition. Deep Speech uses the Connectionist Temporal Classification (CTC) loss function to predict the speech transcript. LAS uses a sequence-to-sequence network architecture for predictions. 

This work is inspired by the recent advances made in end-to-end models. We aim to create an iteration of the state-of-the-art Deep Speech 2 model and modify certain architectural choices in the model to create an improved version. We aim to improve upon the DeepSpeech2 architecture by introducing certain key architectural changes. The block diagram outlines the architecture of our model:

![architecture](https://github.com/SidharthBhakth/E599-high-performance-big-data/blob/master/fall-2020/4/images/Model.png)

### Results:

The improved model achieves an average word error rate of 0.26 on a test minibatch of 10 audio signals after training for 20 epochs, a 42% improvement on the baseline model performance.

| Attempt                | Average CER | Average WER |
| :--------------------- | -----------:| -----------:|
| Deep Speech 2          | 0.17        | 0.45        |
| Improved Deep Speech 2 | 0.1         | 0.26        |


The results are shown below:

![Avg. Character Error Rate](https://github.com/SidharthBhakth/E599-high-performance-big-data/blob/master/fall-2020/4/images/cer.png)
![Avg. Word Error Rate](https://github.com/SidharthBhakth/E599-high-performance-big-data/blob/master/fall-2020/4/images/wer.png)
