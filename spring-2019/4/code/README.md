Our application has the following dependencies:

Log file
Apache MQTT
Apache Storm
Zookeeper
Execution instructions:

Run Zookeeper

Run MQTT, Nimbus, Supervisor and Storm UI

Build a jar file of the project mvn clean compile assembly:single

Deploy the topology (.yaml) file ~/Storm/apache-storm-1.0.4/bin/storm jar Indycar500-testbed-1.0-SNAPSHOT-jar-with-dependencies.jar org.apache.storm.flux.Flux --remote testbed-1carHTM.yaml

Publish the data from the log file java -cp jar name with location com.dsc.iu.utils.RealPublisher
