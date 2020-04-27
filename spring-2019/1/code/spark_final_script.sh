#!/bin/bash

nE=38
coresPerWorker=2
mE=8G


# change username with your username
#data=/user/sumish/data/kmeans-P200000-D10.txt
#data=/user/sumish/data/AirlineReduced
#data=/N/u/sumish/pyspark-example/AirlineReduced
# example:
# data=/user/sakkas/data/kmeans-P200000-D10.txt
#centroids=20
#iteration=10

#spark-submit --deploy-mode client --num-executors ${numExecutor} --executor-cores ${coresPerWorker} --executor-memory ${memExecutor} ./kmeans.py ${data} ${centroids} ${iteration}

for nE in {2..48..2}
do
    for mE in 2G 4G 6G 8G
    do
spark-submit --deploy-mode client --num-executors ${nE}  --executor-cores ${coresPerWorker} --executor-memory ${mE} ./rf.py
    done
done

#spark-submit --deploy-mode client --num-executors ${numExecutor} --executor-cores ${coresPerWorker} --executor-memory ${memExecutor} ./own_forest_paralleising.py
