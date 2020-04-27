# -*- coding: utf-8 -*-
"""
Created on Fri Feb 22 13:58:54 2019

@author: nithish k
"""
import numpy as np
import random
from sklearn.model_selection import train_test_split

import time 

from sklearn.metrics import accuracy_score
from sklearn.metrics import f1_score

import pandas as pd

import forest

airlineData =  pd.read_csv("AirlineReduced")
##exclude not required columns
airlineData = airlineData.loc[:, ~airlineData.columns.isin(['DepTime',
                                                            'ArrTime', 
                                                            'CRSArrTime',
                                                            'CRSDepTime',
                                                            'ActualElapsedTime',
                                                            'ArrTimeInMins',
                                                            'ArrDelay'])]

print(airlineData.head())
X_train, X_test, y_train, y_test = train_test_split(airlineData.loc[:, ~airlineData.columns.isin(['IsDelayed'])], airlineData['IsDelayed'], test_size=0.25, random_state=42)
X_train, X_test, y_train, y_test = np.array(X_train),np.array(X_test),np.array(y_train),np.array(y_test)

myForest = forest.forest(maxDepth=5,numTrees= 10,verbose = True)


start = time.time()
myForest.trainForest(X_train,y_train)

finalPredictions = myForest.predict(X_test)


print("Time elapsed is ", time.time()-start)
print("Accuracy is: " ,f1_score(y_test,finalPredictions))