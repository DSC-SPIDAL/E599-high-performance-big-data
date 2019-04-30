import tensorflow as tf
import numpy as np
import pandas as pd
import math
import os
import time
from sklearn.model_selection import train_test_split

#tf.logging.set_verbosity(tf.logging.DEBUG)
#start=time.time()
# reading data , its an 2006 airline data 
# can be got at http://stat-computing.org/dataexpo/2009/the-data.html
df = pd.read_csv("~/project/2006.csv")

# programmitically calculating departure delay to boolean values
df[-1] = np.where(df['DepDelay']>=0, 1, 0)


#test train data split
#train, test = train_test_split(df, test_size=0.2)
train,one = train_test_split(df, test_size=0.08)
test,two=train_test_split(df, test_size=0.02)

#pruning by selecting only the relevent data points as many of the data points did not provide relevent data  

X_train = train[train.columns[[3,4,5,6,7,9,11,12,13,14,18,19,20]]].values
X_test = test[test.columns[[3,4,5,6,7,9,11,12,13,14,18,19,20]]].values

#X_train = train[['Year',
#             'Month',
#             'DayofMonth',
#             'DayOfWeek',
#             'DepTime',
#             'CRSDepTime',
#             'ArrTime',
#             'CRSArrTime',
#             'FlightNum',
#             'ActualElapsedTime',
#             'CRSElapsedTime',
#             'AirTime',
#             'Distance',
#             'TaxiIn',
#             'TaxiOut',
#            'LateAircraftDelay']].values

#X_test = test[['Year',
#             'Month',
#             'DayofMonth',
#             'DayOfWeek',
#             'DepTime',
#             'CRSDepTime',
#             'ArrTime',
#             'CRSArrTime',
#             'FlightNum',
#             'ActualElapsedTime',
#             'CRSElapsedTime',
#             'AirTime',
#             'Distance',
#             'TaxiIn',
#             'TaxiOut',
#            'LateAircraftDelay']].values

y_train = train[test.columns[-1]].values
y_test = test[test.columns[-1]].values

#storing the start time of processing
start=time.time()

#setting up parameters
params = tf.contrib.tensor_forest.python.tensor_forest.ForestHParams(
  num_classes=2, num_features=5, num_trees=25, max_nodes=25, split_after_samples=50,inter_op_parallelism_threads=48).fill()

#Creating the estimator  
classifier = tf.contrib.tensor_forest.client.random_forest.TensorForestEstimator(
    params)#, model_dir="~/project/models")


#training the model
classifier.fit(x=X_train, y=y_train)

#predicting 
y_out = list(classifier.predict(x=X_test))
n = len(y_test)
out = list(y['classes'] for y in y_out)

#adding the prediction and the value to predicted for easier comparision fromn a list
soft_zipped = list(zip(y_test, out))

num_correct = sum(1 for p in soft_zipped if p[0] == p[1])

#calculating accuracy
print("Accuracy = %s" % (num_correct / n))
end=time.time()
print(end-start)
