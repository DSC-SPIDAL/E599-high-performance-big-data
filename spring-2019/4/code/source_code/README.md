Our application has the following dependencies:

1. Log file 
2. Apache MQTT
3. Apache Storm
4. Zookeeper

Execution instructions:
1. Run Zookeeper
2. Run MQTT, Nimbus, Supervisor and Storm UI

3. Build a jar file of the project
mvn clean compile assembly:single
 
4. Deploy the topology (.yaml) file 
~/Storm/apache-storm-1.0.4/bin/storm jar Indycar500-testbed-1.0-SNAPSHOT-jar-with-dependencies.jar org.apache.storm.flux.Flux --remote testbed-1carHTM.yaml

5. Publish the data from the log file
java -cp *jar name with location* com.dsc.iu.utils.RealPublisher



 

