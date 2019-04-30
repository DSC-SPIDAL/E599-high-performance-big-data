## Instructions

## Steps to run RandomForest on Spark

1. Create a two or three node hadoop cluster.
Refer to tutorial online to setup a hadoop cluster.
(https://letsdobigdata.wordpress.com/2014/01/13/setting-up-hadoop-1-2-1-multi-node-cluster-on-amazon-ec2-part-2/)

2.Configure the cluster to create data nodes and secondary name nodes.

3. Verify the two node configuration of Hadoop.

4. Install apache spark and configure apache spark on top of hadoop cluster. 

5. Install required libraries mentioned in the imports.

6. Upload Script, Data and Apache Spark code.

7. Run the code by running the script, run.sh and make parametric tuning in the code accordingly as per the configuration of your machine.

## Steps to run RandomForest on Tensorflow

1. Install pip and virtualenv depending on Python 2 or 3:
$ sudo apt-get install python-pip python-dev python-virtualenv
$ sudo apt-get install python3-pip python3-div python-virtualenv

2. In the second step, we will create a virtual environment with the following commands shown for Python 2 and 3:
$ virtualenv -–system-site-packages <Directory Name> <br>
$ virtualenv -–system-site-packages –p python3 <Directory Name> <br>
  
3. In the third step, we have to activate our virtual environment with the following commands:
$ source ~/tensorflow/bin/activate
  
4. In our fourth step we will install TensorFlow in the active virtuanenv, with different commands show for CPU Python 3:
$ Pip install –upgrade tensorflow <br>
$ Pip3 install –upgrade tensorflow <br>

Upload the tensorforest code, set and tune the parameters in the code and run.
