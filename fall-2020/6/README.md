# Project Name :- Real time car crash detection for indy car time series data

Group Members :- Akhil Nagulavancha 

*Project Description :-* 

The initial goal of the project is to predict car using Telemetry Data. After exploring the current data available data sets for car crashes and over the course of the project the goal of the project shifted and  I proceeded to work on detecting car crashes and sucessfully able to predict anaomalies for a given car

Deep Learning as come a long way with the advent various Neural Network Architectures like RNN , LSTM etc... LSTM is sucessfully able to model the time series data with out any problems like Gradient explosion and Gradient Vanishing. Autoencoders are type neural networks which were able to copy input to output by ignoring the non relavent aspects of the data. Autoencoders reduce the deimension of the data through encoder and retrive the signal back by decoder. 

So LSTM Autoencoder's were used in this project model the time series data. 

# *Data :-* Relevent Telemetry Dataset is extarcted from Indy Car Log file.

<img width="696" alt="image" src="https://user-images.githubusercontent.com/16973298/102233999-da589180-3f16-11eb-9681-3d7a91db59c9.png">

Detailed Discription of data.

<img width="838" alt="image" src="https://user-images.githubusercontent.com/16973298/102234078-f0665200-3f16-11eb-85ed-471ee6e8acaa.png">

# *Few Results for car-12 :-*

Autoencoder Prediction for vehicle speed test Data 

<img width="851" alt="image" src="https://user-images.githubusercontent.com/16973298/102234348-33282a00-3f17-11eb-9fe8-1848c6a2f418.png">

Autoencoder Prediction for Engine  speed test Data 

<img width="834" alt="image" src="https://user-images.githubusercontent.com/16973298/102234373-3cb19200-3f17-11eb-9c4e-de887b26247f.png">

# Autoencoder Prediction for Lap Distance test Data 

<img width="880" alt="image" src="https://user-images.githubusercontent.com/16973298/102234433-4a671780-3f17-11eb-9569-25ee26867c6e.png">

# Detected Anomlies for car-12 for at throshold 0.96 of the scores.

<img width="404" alt="image" src="https://user-images.githubusercontent.com/16973298/102235037-eb55d280-3f17-11eb-8a8f-dd1d5742eb2f.png">


