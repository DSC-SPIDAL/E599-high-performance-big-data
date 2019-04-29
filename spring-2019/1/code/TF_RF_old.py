#!/usr/bin/env python
# coding: utf-8

# In[1]:


from __future__ import print_function
import numpy as np
import sklearn
import pandas as pd 
import tensorflow as tf
from tensorflow.contrib.tensor_forest.python import tensor_forest
from tensorflow.python.ops import resources
import time

# In[2]:


# read the csv file
data = pd.read_csv('combined_new.csv')
#del data["Unnamed: 0"]
#data.head()


# In[3]:


# split into train (70%) and test (30%)
msk = np.random.rand(len(data)) < 0.7
train = data[msk]
test = data[~msk]


# In[5]:


print(train.shape)
print(test.shape)


# In[6]:


input_x = train.iloc[:, 0:51].values
input_x.shape


# In[7]:


input_y = train.iloc[:, -1].values
input_y.shape


# In[11]:


test_x = test.iloc[:, 0:51].values
test_x.shape


# In[12]:


test_y = test.iloc[:, -1].values
test_y.shape


# In[ ]:




# In[14]:

for tree in range(100, 110, 10):
	
	start = time.time()

	num_steps = 100 # total number of steps for training

	num_classes = 13 

	num_features = 51 

	num_trees = tree 

	max_nodes = 1000 

	tf.reset_default_graph()


	X = tf.placeholder(tf.float32, shape=[None, num_features])

	Y = tf.placeholder(tf.int64, shape=[None])



	# Tensor Forest Parameters

	hparams = tensor_forest.ForestHParams(num_classes=num_classes, num_features=num_features, num_trees=num_trees, max_nodes=max_nodes).fill()

	forest_graph = tensor_forest.RandomForestGraphs(hparams)



	# Get training graph and loss

	train_op = forest_graph.training_graph(X, Y)

	loss_op = forest_graph.training_loss(X, Y)



	# Measure the accuracy

	infer_op, _, _ = forest_graph.inference_graph(X)

	correct_prediction = tf.equal(tf.argmax(infer_op, 1), tf.cast(Y, tf.int64))

	accuracy_op = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))



	# Initialize the variables and forest resources

	init_vars = tf.group(tf.global_variables_initializer(), resources.initialize_resources(resources.shared_resources()))

	sess = tf.Session(config=tf.ConfigProto(intra_op_parallelism_threads=1, 
                        inter_op_parallelism_threads=48, 
                        allow_soft_placement=True))
	sess.run(init_vars)


	for i in range(1, num_steps + 1):

    		_, loss = sess.run([train_op, loss_op], feed_dict={X: input_x, Y: input_y})

   		if i % 50 == 0 or i == 1:

        		train_acc = sess.run(accuracy_op, feed_dict={X: input_x, Y: input_y})
        		test_acc = sess.run(accuracy_op, feed_dict={X: test_x, Y: test_y})

        		print('Step %i, Loss: %f, Acc: %f' % (i, loss, train_acc))
        		print('Test Accuracy: %f' % (test_acc))




	# In[ ]:


	end = time.time()
	print("Time taken for "+ str(tree) + " trees for execution (seconds)", str(end-start))


