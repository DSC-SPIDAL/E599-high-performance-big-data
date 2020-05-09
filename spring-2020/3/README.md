## Real-Time Anomaly Detection for IndyCar Datasets with LSTM

In this project, the state-of-the-art deep learning method-Long short-term memory (LSTM) architecture is used as DL framework to build a model, and then train the log datasets with multi-features (variables) to obtain a speed-predict model, and lastly compare the predict speed with ground truth to determine if something anomaly would happen.

#### Group member: Yafei Wang

#### Datasets pre-processing

The raw datasets `IPBroadcaster_Input_201805-27_0.log` is cleaned, extracted two features (`speed` and `lap_distance`), and then normalized. Here, a `create_dataset` function is defined to creat `data_x` and `data_y` as following: 

```python
def create_dataset(used_dataset, look_back):   
    data_x, data_y = [], []
    for i in range( len(used_dataset)-look_back-1):
        temp = used_dataset[i:(i+look_back), :]
        temp = np.reshape(temp, (temp.shape[0]*temp.shape[1], ))
        data_x.append(temp)
        data_y.append(used_dataset[i+look_back,0])
    return np.array(data_x), np.array(data_y)
```
In the project, the first `10%` of datasets is used as training and following another `5%` as test.


#### Model Architecture

In this project, `Keras (https://keras.io/)` is used to build a LSTM network. The core architecture is listed as following:

```python
model = Sequential()
model.add(LSTM(32, input_shape=(look_back, features), return_sequences=True))
model.add(LSTM(32))
model.add(Dense(1))
model.compile(loss='mae', optimizer='adam') 
```

#### Experiments

In this section, the train and predict results are compared and discussed with three different `timestep` or `look_back` (10, 50, 100).
Here, some example results such as loss, predict speed vs ground truth and anonmaly detection are shown as following:

![loss (timestep=100](https://github.com/yafeiwang89/E599-high-performance-big-data/blob/master/spring-2020/3/code/output_data/timestep%3D100/loss%20during%20training.png)

![predict vs real (timestep=100](https://github.com/yafeiwang89/E599-high-performance-big-data/blob/master/spring-2020/3/code/output_data/timestep%3D100/real%20vs%20prediction%20for%20car%203.png)

![anonmaly detection (timestep=100](https://github.com/yafeiwang89/E599-high-performance-big-data/blob/master/spring-2020/3/code/output_data/timestep%3D100/Anomaly%20detection%20for%20car%203%20(threshold%3D0.08).png)


#### Discussion
In this project, LSTM network is used as DL framework to build a model to train and predict with real-time, multi-features IndyCar datasets. Here, we conclude as we have found and propose some open questions or discussion.
* From the results, the predict speed is very close to the ground truth
* By increasing timestep, the problem that prediction exceed 1.0 is alleviated
* Some open discussion
  * If car location information is available, it would be useful to obtain more accurate results
  * The image datasets is possible used to do augmentation, and then combine telemetry datasets to build model
  * Other possibility such as labelling car crashing should be more effective, but it’s time consuming to manually label it.       What’s more, only 9 cars didn’t finish in 2018, so the crash datasets may be too small? Possible some techniques such as       GAN could be used to generate more new data?








