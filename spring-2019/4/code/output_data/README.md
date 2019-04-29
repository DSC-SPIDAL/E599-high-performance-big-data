The sink files are generated from the Storm Application.
Based on the Car Combination each car will have a separate sink file.
We used this sink file in combination with the IP Broadcast file to generate a metric based csv file with details like score and latencies.
The sink file fields are:
Car Number
Counter
Metric
Current Time- Timestamp
Current Time- Bolt Timestamp
Current Time
Bolt Timestamp
Timestamp
Bolt Timestamp - Timestamp
System Nano Time - Nano Sec
Score

After the excuting python code to parse the IPBroadcast logs we get a metric_name.csv file `throttle.csv` `engineSpeed.csv` `vehicleSpeed.csv`
This file has combined fields of sink and IPBroadcast
We used these csv files for analysis in Tableau.

 

