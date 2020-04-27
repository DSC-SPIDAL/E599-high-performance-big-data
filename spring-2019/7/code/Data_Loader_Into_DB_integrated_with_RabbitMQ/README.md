The code in this folder contains the programs to insert data into MongoDB database after sending and collecting messages from RabbitMQ Queue.



We have 2 separate folders 1)data_loader(for loading the data on rabbitMQ and reading the data from RabbitMQ and inserting into the database)

inside the folder first untar both the folders indy folder is used to read the log file and upload the data onto rabbitMQ Please update the ip address in App.java of the server where rabbitmq is running

indy_Receive_FINAL is the folder which is used to read messages from the Queue and upload the data into the Database

Please update the IP address for both RabbitMQ and MongoDB too

you can build both the packages using the command mvn clean package

once done go to target folder and run incase of both files java -jar indy-0.0.1-SNAPSHOT-jar-with-dependencies.jar*
