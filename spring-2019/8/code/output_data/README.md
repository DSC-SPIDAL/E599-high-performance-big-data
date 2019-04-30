The results are tabulated. Open them in Microsoft Excel or Numbers and Microsoft Word and Pages

1. Sequence Models provide a better accuracy than
Naïve Bayes but they are computationally very
expensive and require lots of training time.

2. Creating more number of batches increases the time
taken for gradient descent to converge.

3. Maximum thread usage appears to be capped at one
half of the number of vCPUs.

4. To achieve the highest accuracy, increasing the
batch size so that there are fewer batches in total
boosts not only accuracy but also drastically
reduces the training time.

5. When there are less number of batches (when size
of each batch is large), it appears to be the case that
gradient descent takes significantly lesser time to
converge.

6. When lesser number of threads are burdened with
more number of mini-batches, the aggregation of
the result of each mini-batch takes a lot of time.
