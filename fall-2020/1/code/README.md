# Instructions for code

### Dependencies

The project requires the following dependencies:
* Python 3
* Conda
* PyTorch
* Zookeeper
* Apache Storm
* Jupyter notebooks

### Instructions to run the code

1. Make sure you have installed all the dependencies.
2. Put the data files in the root of the source_code folder.
3. Run the telemetry_cleaning_vrmath.ipynb to clean the data.
4. The notebook Anomaly_Detection_PyTorch_mse_rmse.ipynb contains the initial LSTM model and the complete single-node workflow along with calculation of mse and rmse values.
5. Use the Anomaly_Detection_Single_Node.py file to run the entire training workflow directly from command line using `python Anomaly_Detection_Single_Node.py`.
6. For distributed training, launch 2 VMs and put the source_code folder on both the VMs. Then use the following command to launch the distributed training:
  
  On VM1: `python -m torch.distributed.launch --nproc_per_node=1 --nnodes=2 --node_rank=0 --master_addr="IP-ADDRESS-OF-VM1" --master_port=1234 Anomaly_Detection_Distributed_Gpu.py`
  
  On VM2: `python -m torch.distributed.launch --nproc_per_node=1 --nnodes=2 --node_rank=1 --master_addr="IP-ADDRESS-OF-VM1" --master_port=1234 Anomaly_Detection_Distributed_Gpu.py`
  
  Use Anomaly_Detection_Gpu.py for using GPUs on the VMs during training. If you want to use only the CPU, use the Anomaly_Detection_Distributed_Cpu.py.
  See the code in these files for command line arguments to customize training parameters. Pass the arguments after filename in the above command.

### Instructions for Storm
1. Put the saved model for distributed GPU version in Lab6 folder. Also, put the formatted.csv (input data) file in the Lab6 folder.
2. Set up port tunneling between the VM and host machine.
3. Install Java and Maven.
4. Install and properly configure zookeeper for VM.
5. Logout of VM and login again using post forwarding.
`ssh -L 5000:localhost:5000 -L 6060:localhost:6060 -L 8080:localhost:8080 VM-address`
5. Run Zookeeper, Storm nimbus, supervisor, and ui. Use tmux to separate sessions.
`~/zookeeper-3.4.14/bin/zkServer.sh start`

`~/apache-storm-2.1.0/bin/storm nimbus`

`~/apache-storm-2.1.0/bin/storm supervisor`

`~/apache-storm-2.1.0/bin/storm ui`

You can see the ui on localhost:8080

6. Run Jupyter-lab
`jupyter-lab --ip0.0.0.0 --port6060`

7. Run the webserver in web-server/.
`pip3 install flask`

`cd ~/Lab6/web-server/python` 

`webserver.py`

8. Clean package: 
`cd ~/Lab6/indycar-storm/`

`mvn clean package`
9. Submit topology:
`storm jar target/IndyCar-1.0-SNAPSHOT.jar org.apache.storm.flux.Flux -l -R /topology.yaml`
10. Go to localhost:5000 in your browser to see results.


