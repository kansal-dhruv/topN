This Service is built to provide real-time top k scores for any game.

There are 4 modules to this service:

1. topn-models: This module consists of all the dependencies required across all the other modules.
2. aggregator-service: This Service takes all influx of the data. Currently, this service is designed in a way that all the data is input and then distributed among a number of queues.<br />
   ℹ️ The Future scope of the service, is to various operations like data de-dup, data aggregation, etc..
3. processor-service: This service takes a partition of data, partitioned by aggregator-service. This service maintains a local maxima for the partition up to top k elements. Once it processes a batch, it produces a message consisting of its local maxima. It used internal memory to store the data in a heap data structure.
4. topN-scores-service: This service is the same, as in the previous step but instead now this listens to all the local maxima for each partition, therefore maintaining a global maxima. Once it processes batch it stores the local maxima to a persistent disk.

Please read module wise readme for module-wise understanding. 