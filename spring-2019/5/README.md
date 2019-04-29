**COMPARISON OF SPARK AND DASK PLATFORMS USING NEURAL NETWORKS**

Team Members: Vatsal Jatakia, Saniya Ambavanekar

Description: 
This project aims at performing an exhaustive comparison between SPARK and DASK platforms using Neural Networks. We try various configurations to see how the computation time and accuracy varies for each of the configurations and see which one is more suited for each platform and extend these findings to conclude which one would be more suited for the application of Neural Networks. We use the following configurations: 

1. Number of cores: 4,5,6
2. Number of executors: 9,7,5
3. Memory per executor: 11g, 13g, 19g

Dataset Description: 

Available here: http://yann.lecun.com/exdb/mnist/index.html

Description: We make use of the MNIST dataset. This dataset is a popular dataset used as a benchmark to judge the performance of a Neural Network Architecture. It consists of 60k training and 10k testing images of size 28 X 28 pixels. This is the dataset that we used for training and testing purposes.

Steps: 
1. Create a SparkContext Object using PySpark. 
2. Load the data 
3. Specify the parameters to update this object and override the default values. 
4. Train the model and track the time 
5. Evaluate the model using the test dataset to calculate the accuracy 
6. Record the computation time and accuracy for each of the configurations and enter into a table for comparison. 

Dask Steps:
1.	We know that the scheduler is main central component of the Dask responsible for distributing the task and coordinating processes. We need to start the scheduler on a node by executing dask-scheduler command. 
2.	We need to create worker processes and also allocate them the number of cores and the memory for each of them, so we execute Dask command such as dask- worker –nprocess n –nthreads p –memory-limit x GB. 
For each of the above configurations, we vary the number of epochs as 10,100,1000,10000 and track the computation time and accuracy. 
The following were the observations: 
1.As the number of cores increases, the accuracy for a specific number of epochs increases suggesting that the computations are becoming more efficient. 

2.There is a tradeoff to be considered here between the accuracy and the computation time in order to decide the most efficient config to be used. 

3.The selection of the ideal config will vary from application to application. For example, a medical application will require emphasis on a good accuracy as compared to time, while a financial application will require faster results as compared to accurate results.


