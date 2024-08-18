The aggregator service takes all the data and then runs the data pipeline on a batch of data.

This service provides an interface to run data pipelines on the given data.
```java
public interface DataPipelineService<T> {
    void executePipeline(List<T> data);
}
```
`RabbitMqDataPipelineService.java` includes the code of the current data pipeline.
This pipeline generates a hash on the `playerId`. Based on the hash value it pushes to the partition queue.<br>

To generate a hash for a String value, the service provides an interface for a given Object.
```java
public interface HashService <I, R>{
    R generateHash(@Valid @NotNull I input);
}
```
`PlayerIdHashService.java` includes the code to generate a hash value for a given `playerId`.
The maximum number of unique hashes to be produced is controlled by the following :
```properties
parallel.queue.workers=<number of queues> #default value = 1
```
Once the hash is calculated, it is pushed to a queue with `queueName=<hash value>` for further processing.


Some of the other controlling flags include:
```properties
rmq.batch.size=<batch  size> #default value = 1000
data.influx.queue=<data influx queue name> #default values = scores-queue
```

The current scope of the service includes:
1. Data Partitioning


The Future scope of the service includes:
1. Data aggregation
2. Data Deduplication