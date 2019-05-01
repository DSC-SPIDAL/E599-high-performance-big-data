Write your group members' name, your project name and put a summary of your project

Group Members:
Rohit Bapat
Amit Makashir

Project Name:
System infrastructure and performance analysis of anomaly detection application IndyCar

Project Summary:
IndyCar is  one  of  the  top-level  car  racing  series  in  America.  During  the  period  of  each race, different kind of events happen, including pit stops, crashes, mechanical breakdown, drivers  ranking  changes,  and  more. With  the  timing  and  scoring  log  data,  the  task  is to detect events interested which can be categorized as the anomaly detection task.

The entire project is mainly divided into 3 phases.
Event detection and prediction of race data.
Performance analysis of anomaly detection application.
Alternatives for message brokers and introducing a NoSQL based data persistence layer.

We analyzed the performance of the anomaly detection application. The data we used for this analysis was stored in a log file. We simulated the real-time conditions to analysis the behavior of the Storm application.

We are using Apache Storm to stream in data coming in at high speeds from the race cars.

After generation of sink files we are using Python scripts to generate metric based csv files.

We are using Tableau Visualization tool visualize the metric and Latencies.