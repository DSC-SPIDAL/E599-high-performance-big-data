## Real-Time Anomaly Detection for IndyCar Datasets with LSTM

#### Group member: Yafei Wang
* Summary: In this project, the state-of-the-art deep learning method-Long short-term memory (LSTM) architecture is used as DL framework to build a model, and then train the log datasets with multi-features (variables) to obtain a speed-predict model, and lastly compare the predict speed with ground truth to determine if something anomaly would happen.
![loss function](https://github.com/yafeiwang89/E599-high-performance-big-data/blob/master/spring-2020/3/code/output_data/timestep%3D100/loss%20during%20training.png)

#### Dataset pre-processing

```python
def create_dataset(used_dataset, look_back):
    
    # scaled = scaler.fit_transform(used_dataset) 
    
    data_x, data_y = [], []
    for i in range( len(used_dataset)-look_back-1):
        temp = used_dataset[i:(i+look_back), :]
        temp = np.reshape(temp, (temp.shape[0]*temp.shape[1], )) # convert matrix to vector
        data_x.append(temp)
        data_y.append(used_dataset[i+look_back,0])

    return np.array(data_x), np.array(data_y)

```

#### Model Architecture

```python

model = Sequential()

model.add(LSTM(32, input_shape=(look_back, features), return_sequences=True))
#model.add(keras.layers.Dropout(rate=0.2))

model.add(LSTM(32))
#model.add(keras.layers.Dropout(rate=0.2))

model.add(Dense(1))

model.compile(loss='mae', optimizer='adam', metrics=["acc"]) # metrics is meaningless here

```
