Spark Configurations:

Config 1: 4 cores, 9 executors, 11 GB memory 
# Epochs	Computation Time	Accuracy
10	      33.6s	            91.64%
100	      4min 25s	        96.92%
1000	    18min 48s	        97.7%
10000	    18min 24s	        97.46%


Config 2: 5 cores, 7 executors, 13 GB memory 
# Epochs	Computation Time	Accuracy
10	      41.2s	            91.7%
100	      4min 24s	        97.09%
1000	    17min 25s	        97.5%
10000	    17min 4s	        97.6%

Config 3: 6 cores, 5 executors, 19 GB memory 
# Epochs	Computation Time	Accuracy
10	      42s	              90.51%
100	      5min 52s	        97%
1000	    17min 7s	        97.4%
10000	    18min 13s	        97.3%


DASK: 

Config: 4 threads, 9 workers, 11GB memory 
# Epochs	Computation Time	Accuracy
10	      9.45 s	           91.3%
100	      1 min 32 s	       95.4%
1000	    5 min 12s	         96.7%
10000	    5 min 12s	          96.7%


Config 5: 5 threads, 7 workers, 13GB memory 
# Epochs	Computation Time	Accuracy
10	      9.21s	            91.3%
100	      1 min 29 s	      95.4%
1000	    5 min 4s	        96.7%
10000	    5 min 2s	        96.7%


Config: 6threads, 5workers, 19GB memory 
# Epochs	Computation Time	Accuracy
10	      9.34s	            91.3%
100	      1 min 8s	        95.4%
1000	    5 min 1s	        96.7%
10000	    5 min 2s	        96.7%





