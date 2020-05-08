PROJECT NAME: <br />
IndyCar PitStop Prediction

GROUP MEMBERS:<br/>
Selahattin Akkas, Ayush Srivastava & Siddhartha Rao

PROJECT SUMMARY:<br/><br/>
The goal of this project is to come up with a model which will predict when a race car in Indianapolis 500 will go for a pit-stop.
Indianapolis 500 is a top-level car racing event in America that takes place every year during the month of May at Indianapolis motor speedway. The race has 33 cars going around the circuit for 200 laps.The distance of 1 lap around the oval is 2.5 miles and the total race is around 500 miles. During the race the cars may have to go to the pitstop in order to fuel up or change their tires. The strategies used for pitstop plays a huge factor in determining the winner ofthe race.
<br/>

Our Input data consists of log files of all 200 cars who took part in Indy500 from the years 2013 to 2019.

Training Data:<br/>
We've used the Log files from the year 2013 to 2017 to train our LSTM Model

Test Data:<br/>
We've used the above trained model to test out when a car in Indy500 race will go to pitstop for 2 years: 2018 & 2019

To figure out the strategies used by drivers  we have created some of our own features like:<br/>
Since_last_pit : Number of laps since the last pit stop<br/>
Since_last_pit_green : Number of green flag laps since the last pit stop<br/>
Since_last_pit_yellow : Number of yellow flag laps since the last pit stop<br/>
Fuel: Fuel consumed after the last pitstop. We have assumed that the vehicle consumes 1 unit of fuel during green flag and 0.3 units of fuel during yellow flag<br/>

For our project we have used a LSTM model with 4 LSTM cells and 1 dense layer. We trained this model for 100 epochs using the training data. The final training loss we got was 0.05 and validation loss was 0.0639. We are trying to predict the number of laps till the next pitstop.<br/>
![epoch vs error](https://github.com/aysriv/E599-high-performance-big-data/blob/master/spring-2020/1/code/output_data/epoch%20vs%20error.png)

We have tested this model on data from 2018 & 2019 and then compared with the available ground truths.<br/><br/>

Below are the graphs obtained for both the years:<br/>
- Yellow flag which took place during the race are depicted by yellow bars on the plot<br/>
- In the images, red line depict the ground truth, i.e. the actual number of laps till the next pitstop.<br/>
- Blue line is our prediction
![2018](https://github.com/aysriv/E599-high-performance-big-data/blob/master/spring-2020/1/code/output_data/2018_final.png)
![2019](https://github.com/aysriv/E599-high-performance-big-data/blob/master/spring-2020/1/code/output_data/2019_final.png)
As you can see our model’s prediction had a high accuracy for 2019 data.

Conclusion:<br/>
- If the yellow flag is at least 20 laps after their previous pitstop ,then the cars will go to the pit lane
- This model does help us in predicting when the cars will go to the pit lane, and we think that if there was more data available the model would have performed better










