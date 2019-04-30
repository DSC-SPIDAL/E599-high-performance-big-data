#!/bin/bash

numExecutor=48
coresPerWorker=1
memExecutor=4G



# change username with your username
#data=/user/sumish/data/kmeans-P200000-D10.txt
data=/user/sumish/data/AirlineReduced
#data=/N/u/sumish/pyspark-example/AirlineReduced
# example:
# data=/user/sakkas/data/kmeans-P200000-D10.txt
#centroids=20
#iteration=10

#spark-submit --deploy-mode client --num-executors ${numExecutor} --executor-cores ${coresPerWorker} --executor-memory ${memExecutor} ./kmeans.py ${data} ${centroids} ${iteration}

#spark-submit --deploy-mode client --num-executors ${numExecutor}  --executor-cores ${coresPerWorker} --executor-memory ${memExecutor} ./sparkMultipleNodeParallel.py

spark-submit --deploy-mode client --num-executors ${numExecutor} --executor-cores ${coresPerWorker} --executor-memory ${memExecutor} ./sparkSingleNodeParallel.py


