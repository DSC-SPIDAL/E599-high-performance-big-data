### File Description ###
The file `IPBroadcaster_Input_2018-05-27_0.log` contains the Sensor Data from the IndyCar Race. The Telemetry document includes the relevance of each line in this log.
We have only included the '$P' records which are related to 3 metrics Speed, Throttle, RPM.
The dataset fields included in the analysis are:
No : Car Number
Time of the Day
Lap Distance
Vehicle Speed
Engine Speed
Gear 0-6
Throttle %

The total samples in the dataset are:
4871355
This is inlcuding the logs for $X for Heart Beats, $M Messages, $P Telemetry, $O Overall Results.
We have parsed just the $P entries in the File.

The sink files generated after successful runs of the Storm Topology essentially are the input files for aggregation with these results for further Latency and Anomaly Score based Analysis.
 
 