The processor service takes all the data from a partition and then processes the data.

This service provides an interface to run data pipelines on the given data.
```java
public interface ProcessorService<T> {
    void processData(List<T> data);
    void postProcess(List<T> data);
}
```
`HeapProcessorService.java` includes the code on how the partitioned data is processed.
This provides a way to store top k elements using heaps implemented using Java's Priority Queues.
The Following properties allow us to change the number of elements this processor stores in memory.
```properties
max.heap.size=<value of k in top k> #default value = 1,000
```

The `processData()` method updates the heap, after which the postProcess() is invoked, This method ensures the local maxima is pushed to some queue for further processing.

```properties
topn.processor.queue=<top-service-consumer-queue> #default value = processed-data-queue
```


Some of the other controlling flags include:
```properties
rmq.batch.size=<batch  size> #default value = 1000
consumer.queue.name=<hash value> #default values = 0 default value to aggregator service produces only 0 as hash value.
```

The current scope of the service includes:
1. Storing local maxima for a partition.


The Future scope of the service includes:
1. Adding a pre-processor to remove all the outliers or false cases before processing them.
2. Can be integrated with Data Analytics tools like Hadoop for better performance over huge amounts of data.