Contains Files for Writing data from log file to DB as well as query modules that were suggested in the course.

mongo-java-driver-3.7.1.jar is the jar file that would have to be included in Java build path to run the following files as intended.

Read_From_Log_to_DB.java - this file contains a function called write_to_db that writes data from log files and filters them before uploading data into intended collections.


------------------ The following files have to be included together included.--------- 
mongoclienttest.java - Contains the function calls to all the functions in DriverService.java, RaceService.java, and TrackService.java

IndycarDBClient.java - Connectes mongoclienttest with the files mentioned below

DriverService.java -Contains the functions for DriverService module

RaceService.java - Contains the functions for RaceService Module

TrackService.java - Contains the functions for TrackService Module


To run the application. 
1.Load the data into MongoDB database 
2.add the database name in the IndycarDBClient file as mentioned in the file
3.Uncomment the query you would like to run in the mongoclienttest file 
4.execute the program to get the results.

Note- All returned values from the functions are gonna have a data type of ArrayList.
