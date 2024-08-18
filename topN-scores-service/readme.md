This Service is built on previous module `processor-module`.
It receives a copy of local maxima for every partition and maintains top k elements using the same heap implementation for the previous module.

```properties
max.heap.size=<value of k in top k> #default value = 1,000
```

`TopNProcessorService.java` includes all the code for processing the data. But the postProcess() in overridden to push the global to MySQL database.
It reads all top score from queue, pushed by our processor services.
The SQL server details and consumer queue name can be configured using the following properties. 
```properties
top-scores-queue=<top scores queueName> //(topn.processor.queue for processor service) default value : top-scores-queue
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=mysqluser
spring.datasource.password=mysqlpass
spring.datasource.url=jdbc:mysql://localhost:3306/myDb?createDatabaseIfNotExist=true
```

This Service provides an REST endpoint to fetch top k scores, configured using `max.heap.size` variable.
This rest endpoint, fetches the data directly from the database within ms.

Some of the other controlling flags include:
```properties
rmq.batch.size=<batch  size> #default value = 1000
```