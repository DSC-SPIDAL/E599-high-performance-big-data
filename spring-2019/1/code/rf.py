
from pyspark import SparkConf, SparkContext
from pyspark import SQLContext
from pyspark.sql.functions import col,avg
import pandas as pd 
from pyspark.sql.types import *

from pyspark.ml.feature import VectorAssembler
from pyspark.sql.functions import lit,udf,monotonically_increasing_id,sum,sqrt
from operator import add
from pyspark.sql.types import *
from pyspark.ml import Pipeline
from pyspark.ml.classification import RandomForestClassifier
from pyspark.ml.feature import IndexToString, StringIndexer, VectorIndexer
from pyspark.ml.evaluation import MulticlassClassificationEvaluator
from pyspark.sql import Row,DataFrameReader
from pyspark.sql.functions import expr
#conf=SparkConf().setMaster("local[*]").setAppName("Querying")
conf = SparkConf().setMaster('yarn').setAppName('Querying')
sc=SparkContext(conf=conf)
sc.setLogLevel("ERROR")
sqlContext=SQLContext(sc)
# Load and parse the data file, converting it to a DataFrame.
pampa = sqlContext.read.csv("combined.csv",header=True,inferSchema=True)
pampa = pampa.withColumn("Activity_id", pampa['activity_id'].cast(IntegerType()))
#print(pampa.show())

#Split dataframe into train-valid-test
(train,test) = pampa.randomSplit([0.7,0.3],1000)

#Dataframe transformation

from pyspark.ml.feature import VectorAssembler
from pyspark.ml.feature import OneHotEncoderEstimator

vec_assembler = VectorAssembler(inputCols=['imu_0',
 'imu_1',
 'imu_2',
 'imu_3',
 'imu_4',
 'imu_5',
 'imu_6',
 'imu_7',
 'imu_8',
 'imu_9',
 'imu_10',
 'imu_11',
 'imu_12',
 'imu_13',
 'imu_14',
 'imu_15',
 'imu_16',
 'imu_17',
 'imu_18',
 'imu_19',
 'imu_20',
 'imu_21',
 'imu_22',
 'imu_23',
 'imu_24',
 'imu_25',
 'imu_26',
 'imu_27',
 'imu_28',
 'imu_29',
 'imu_30',
 'imu_31',
 'imu_32',
 'imu_33',
 'imu_34',
 'imu_35',
 'imu_36',
 'imu_37',
 'imu_38',
 'imu_39',
 'imu_40',
 'imu_41',
 'imu_42',
 'imu_43',
 'imu_44',
 'imu_45',
 'imu_46',
 'imu_47',
 'imu_48',
 'imu_49',
 'imu_50'],outputCol='VectorFeatures')

#Random Forest classifier
from pyspark.ml.classification import RandomForestClassifier
from pyspark.ml import Pipeline
from pyspark.ml.tuning import CrossValidator,ParamGridBuilder
from pyspark.mllib.evaluation import BinaryClassificationMetrics
from pyspark.ml.evaluation import BinaryClassificationEvaluator
import time
from pyspark.ml.evaluation import MulticlassClassificationEvaluator

classifier = RandomForestClassifier(labelCol='Activity_id',featuresCol='VectorFeatures',numTrees=200)
int_pipe = Pipeline(stages=[vec_assembler,classifier])
starttime = time.time()
model = int_pipe.fit(train)
prediction = model.transform(test)
t = time.time() - starttime
print("Time Taken = ", t)
acc_eval  = MulticlassClassificationEvaluator(metricName='accuracy', labelCol='Activity_id')
a = acc_eval.evaluate(prediction)
print("Accuracy = ", a)
f = open("/N/u/risnaga/time.txt", 'a+')
f.write("Time Taken = " + str(t) + '\n')
f.close()
f = open("/N/u/risnaga/accu.txt", 'a+')
f.write("Accuracy = " + str(a) + '\n')
f.close()
