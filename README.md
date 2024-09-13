# Projects related to Apache Spark SQL/Streaming, Kafka technologies and MapReduce.
- 'mapreduce' = classes Mapper, Reducer, Job solve a simple bigdata task by parallelizing and splitting for parallel processing on several daemon-processes inside Hadoop;
- 'sparksql' = solve problem using Spark SQL technology (3 tables, 5 queries with data from CSV, JSON; saving in Parquet and Avro formats);
- 'jsender' = server-app for realtime transmission of JSON-object data by websockets (to separate part of the 'sparksql');
- 'kafkaproducer' = microservice app - stream source for Kafka (async message-production in K.topics);
- 'kafkasparkstr' = microservice app - streaming messages from Kafka topic (Spark Structure Streaming) and writing to another topic on the broker. Capable of working in real time with 'kafkaproducer';
- 'kafkaconsumer' = microservice app - consumer (listener) of messages from K.topics to write data to NoSQL database (MongoDB).
___
Using Java and Spring to development.
