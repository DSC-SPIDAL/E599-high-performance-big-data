
### Group members: 
  
  Arpit Rajendra Shah
  
  Karen Sanchez Trejo
  
  Shilpa Singh

### Project name:
    Latent Dirichlet Allocation for Large Scale Data

### Summary: 

The objective of this project is comparing the two LDA algorithms that are implemented in Spark (EM and Online), finding which one has the best computational performance and which one gives the best model. EM algorithm learns clustering using MAP Estimation, while Online uses iterative mini-batch sampling for Online Variational Inference. 

We carried out experiments using the Yelp dataset, which contains up to 5GB of reviews from Yelp users.  We did the following text pre-processing methods: Text-Tokenization, removing Stop Words, POS tagging, we selected only the nouns to get a better model and we converted the text into vector of Token Counts. We divided the dataset in train and test, measuring the quality of the model with the perplexity of the test dataset, the lower the perplexity, the better the model.

We found out that EM converges faster, however we got better results (in terms of test perplexity) with Online.  EM gives an overflow error after 100 iterations and doesn't consider asymmetric priors. In general, we could say Online is a better algorithm, but it is also the slowest which could be explained by the fact that it a more complicated than EM. 

We fined tuned EM for different spark configurations. The resource allocation strategy for running spark experiments was to divide the total number of cores (only 24 taken as more cores were causing more network shuffling) and memory(only 62GB taken) on a node among the executors where initially each executor got most of the cores (20) and memory(60GB) resulting in a smaller number of executors(3) throughout the cluster. Then we decreased the number of allocated cores to 1 for each executor and memory (5GB) to each executor resulting in a greater number of executors (30) in the cluster. The optimal performance we got for EM is between these 2 extreme conditions where the cores allocated to each executor was 3 and memory 15GB with total number of executors as 12. The reason for the medium sized executor being the most optimal was that when we create too less executors, we impose a limit on the degree of parallelism across map partitions as even though they have more cores to execute tasks in parallel , they are forced to work on the same data partition which is a condition enforced by spark. But, when we create more executors, though we have more executors to map to these data partitions, the memory allocated to each executor is reduced which caused lot of shuffling and delays in garbage collection step in the spark execution.If we keep the memory of each executor more and increase the number of executors,there is more network shuffling. So, the optimal value was achieved for medium sized executor where there were enough executors to map to data partitions in parallel without causing any shuffling in the execution.

For online,the execution time was agnostic of the executor settings we tried for EM.It took approx.30 minutes without POS tagging to complete for all settings and 5 hours with POS tagging on 1 GB of data. This was because of instantiating new sentence object for nlp processing inside every record of the data frame, which was computationally expensive.
