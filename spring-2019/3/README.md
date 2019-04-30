Title: Distributed Random Forest

Group members:

Srinithish Kandagadla

Sumith Mishra

Summary:
We implemented a Decision tree from scratch aswe wanted greater
control over the parameters and functions like getting the best
splitting criteria over a variable. Of-the-shelf implementation like
Sci-kit Learn, do not give this freedom or its too hard to tweak
their functions due to dependencies. Hence we decided to write our
own decision tree implementation. We have built a decision tree
for classification problems. Randomization of the decision tree in a
random forest can be achieved by subsampling the data points and
considering a subset of the variables or features for a split in a tree.

We have parallelised with tensor flow(horovod) and Spark majorly using python 
