To run spark,


1. Open the run basch script,
2. For running spark single node 
   Navigate to the folder where `AirlineReduced` file (data is placed)
   Place the script 'sparkSingleNodeParallel.py' in the same folder
   Uncomment the code line in the script
   
   `spark-submit --deploy-mode client --num-executors ${numExecutor} --executor-cores ${coresPerWorker} --executor-memory ${memExecutor} ./sparkSingleNodeParallel.py`
   
3. For running spark on multiple nodes
   Navigate to the folder where `AirlineReduced` file (data is placed)
   Place the script `sparkMultipleNodeParallel.py` in the same folder
   Uncomment the code line in the script
   
   `spark-submit --deploy-mode client --num-executors ${numExecutor} --executor-cores ${coresPerWorker} --executor-memory ${memExecutor} ./sparkMultipleNodeParallel.py`

   
To run Tensorflow with horovod,

1. Place the data `AirlineReduced` in the same folder as the script `Horovod_forest_tensor_flow.py`
2. Install Horovod if not already installed 
	https://github.com/horovod/horovod#install
	run this command pip install horovod
3. Install MPI

		`
		shell$ gunzip -c openmpi-4.0.1.tar.gz | tar xf -
		shell$ cd openmpi-4.0.1
		shell$ ./configure --prefix=/usr/local
		<...lots of output...>
		shell$ make all install
		`
		
3. Run the fllowing command, note the the nodes have to be changed accordingly, change the number of total processes spawned
	`mpirun -np 10 -H j-026:5,j-027:5 -bind-to none -map-by slot -x LD_LIBRARY_PATH python Horovod_forest_tensor_flow.py`
		
		
