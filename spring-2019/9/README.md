

## Random Forest for Big Data

Over the last ten years, popularity in MapReduce and large-scale data processing has led to the emergence of wide variety of cluster computing frameworks. With rich language-integrated APIs and wide range of libraries Apache Spark has become the de facto open source big data processing framework. Along with big data, comes machine learning libraries for differential programming on huge datasets. One of the most popular libraries is TensorFlow introduced by Google Brain team. Focusing on big data classification problem, our project aims to do a selective review and performance comparison through experiments by analyzing distributed implementation of Random Forest algorithm on Apache Spark and sequential Random Forest algorithm using machine learning libraries of TensorFlow.

We implement a popular classification algorithm – Random Forest on real world data and showed how it can be scaled in a distributed environment using Apache Spark. We also showed and benchmarked the sequential performance of the algorithm by comparing the performance of Apache Spark’s mllib library and Tensorflow’s tensorforest in terms of execution time and accuracy.

With our initial results we conclude that MLlib implementation of Random Forest is faster than the Tensorforest estimators owing to the in-memory computation of Spark’s executors. Transformations are very powerful characteristic of Spark. It comes with very
advanced Directed Acyclic Graph (DAG) or a data processing engine that is more suited for classification problems. However, unlike TensorFlow, Spark does not have any out of the box support for deep neural network architectures such as Convolutional Neural Network for images and Recurrent Neural Network.

Group Members:

Jainendra Kumar 
Gattu Ramanadham
Surya Prateek Soni
