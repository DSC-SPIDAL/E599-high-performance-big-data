

Group Members: Sahaj Singh Maini, Ishneet Singh Arora

Project Name: Resolving latency issues of the message flow and implementing the data persistence layer (IndyCar)

Project Summary: IndyCar is one of the top-level car racing series in America. During the period of each race, different kind of events happen, including pit stops, crashes, mechanical breakdown, drivers ranking changes, and more. With the timing and scoring log data, the task is to detect events interested which can be categorized as the anomaly detection task.

The entire project is mainly divided into 3 phases. Event detection and prediction of race data. Performance analysis of anomaly detection application. Alternatives for message brokers and introducing a NoSQL based data persistence layer.

We were working on benchmarking the current Apache Apollo Messaging broker against RabbitMQ so that we could find an alternative and also made use of MongoDB Database for storing the data which we receive from Indianapolis 500 race to support query and analysing .

Message Broker Implementation

For this project, we have used log files which contains the real data of the Indianapolis 500 race. We are reading the data from these files and sending each line of the log file which represents a record as a message to a RabbitMQ Queue. A consumer then subscribes to this queue and reads the messages, filters and preprocesses them before storing the messages into an array. After every minute, the messages from the queue are then sent for insertion into the database. For this particular setup, we have installed RabbitMQ on an Open Stack Server. We have also installed MongoDB on three OpenStack Servers. The data in the MongoDB database across the three servers is synchronized and replicated for better data availability as well as for making the system fault tolerant.

Database Implementation

The messages from the messaging broker are pre-processed and arranged in the required order before being inserted in the respective MongoDB database. The version of the Mongo-Java used for the project is mongo-java-driver-3.7.1. The data is divided and stored in multiple collections depending upon the type of record that is received from the messaging broker and the information that the record contains. The record is inserted into its respective collection along with the race id of the race that the record contains information about. Every section in a record forms a field in the MongoDB document that is then inserted in the respective MongoDB collection. The current implemented Database for the persistence layer contains eight different collections which are CompletedLaps, CompletedSection, EntryInfo, FlagInfo, OverallResults, RunInfo, TrackInfo, and telemetry.
