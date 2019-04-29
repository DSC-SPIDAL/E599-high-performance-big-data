# IMPORTING MODULES 
import warnings
warnings.filterwarnings("ignore")
from sklearn import grid_search, datasets
from sklearn.neural_network import MLPClassifier
from spark_sklearn import GridSearchCV
from keras.datasets import mnist
import numpy as np
from pyspark import SparkContext
from pyspark.conf import SparkConf
from pyspark.sql import SparkSession

# Creating a SPARK Context object 
sc = SparkContext()


# Loading the MNIST dataset
(X_train, y_train), (X_test, y_test) = mnist.load_data()
X_train = X_train/256
X_test = X_test/256

# Reshaping the data 
train = np.reshape(X_train, (X_train.shape[0], X_train.shape[1]*X_train.shape[2]))
test = np.reshape(X_test, (X_test.shape[0], X_test.shape[1]*X_test.shape[2]))

# Specifying the Spark Parameters 
exec_mem = ["11g","13g","19g"]
exec_cores = ["4","5","6"]
exec_inst = ["9","7","5"]
exec_config = list(zip(exec_mem, exec_cores, exec_inst))
iter_list = [10,100,1000,10000]

################# TRAINING PROCESS #######################

#### CONFIG 1 ########
j = exec_config[0]
print('-----------------  Config = ',j,' -------------------------')
conf = sc._conf.setAll([('spark.executor.memory', j[0]), ('spark.executor.cores', j[1]),('spark.executor.instances', j[2])])
spark = SparkSession.builder.config(conf=conf).getOrCreate()
print(sc._conf.getAll())

for i in iter_list:
    print('--------------------Iterations = ',i,'-----------------------')
    param_grid = {"solver": ["sgd"],
                  "max_iter" : [i],
                  "hidden_layer_sizes": [(100,10)],
                 }
    gs = GridSearchCV(sc, estimator = MLPClassifier(), param_grid=param_grid)
    print('Time info for iterations = ',i)
    get_ipython().run_line_magic('time', 'gs.fit(train, y_train)')

    preds = gs.predict(test)
    print('Accuracy=',np.sum(y_test == preds)*100/len(y_test),'%')


#### CONFIG 2 ########
j = exec_config[1]
print('-----------------  Config = ',j,' -------------------------')
conf = sc._conf.setAll([('spark.executor.memory', j[0]), ('spark.executor.cores', j[1]),('spark.executor.instances', j[2])])
spark = SparkSession.builder.config(conf=conf).getOrCreate()
print(sc._conf.getAll())

for i in iter_list:
    print('--------------------Iterations = ',i,'-----------------------')
    param_grid = {"solver": ["sgd"],
                  "max_iter" : [i],
                  "hidden_layer_sizes": [(100,10)],
                 }
    gs = GridSearchCV(sc, estimator = MLPClassifier(), param_grid=param_grid)
    print('Time info for iterations = ',i)
    get_ipython().run_line_magic('time', 'gs.fit(train, y_train)')

    preds = gs.predict(test)
    print('Accuracy=',np.sum(y_test == preds)*100/len(y_test),'%')



#### CONFIG 3 ########
j = exec_config[2]
print('-----------------  Config = ',j,' -------------------------')
conf = sc._conf.setAll([('spark.executor.memory', j[0]), ('spark.executor.cores', j[1]),('spark.executor.instances', j[2])])
spark = SparkSession.builder.config(conf=conf).getOrCreate()
print(sc._conf.getAll())

for i in iter_list:
    print('--------------------Iterations = ',i,'-----------------------')
    param_grid = {"solver": ["sgd"],
                  "max_iter" : [i],
                  "hidden_layer_sizes": [(100,10)],
                 }
    gs = GridSearchCV(sc, estimator = MLPClassifier(), param_grid=param_grid)
    print('Time info for iterations = ',i)
    get_ipython().run_line_magic('time', 'gs.fit(train, y_train)')

    preds = gs.predict(test)
    print('Accuracy=',np.sum(y_test == preds)*100/len(y_test),'%')





