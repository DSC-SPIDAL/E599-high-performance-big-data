put short description for the input data. For example:  Number of samples, features 

Do not include any confidential data.

Our Input data consists of log files of all 200 cars who took part in Indy500 from the years 2013 to 2019.

We have created some of our own features like:

- Since_last_pit : Number of laps since the last pit stop
- Since_last_pit_green :   Number of green flag laps since the last pit stop
- Since_last_pit_yellow : Number of yellow flag laps since the last pit stop
- Fuel: Fuel consumed after the last  pitstop. 
        We have assumed that the vehicle consumes 1 unit of fuel during green flag and 0.3 units of fuel during yellow flag

Our number of samples are:

For each year the number of samples are 200*33 (200 laps & 33 cars) for 7 years, i.e. 2013-2019

Training Data:

We've used the Log files from the year 2013 to 2017 to train our LSTM Model

Test Data:

We've used the above trained model to test out when a car in Indy500 race will go to pitstop for 2 years: 2018 & 2019
