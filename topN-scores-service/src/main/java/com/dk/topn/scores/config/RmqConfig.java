package com.dk.topn.scores.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class RmqConfig {

    @Value("${rmq.batch.size:1000}")
    private int batchSize;

    @Value("${rmq.concurrency:1}")
    private int concurrency;

    final TaskExecutor executor;

    public RmqConfig(@Qualifier("virtualThreadExecutor") TaskExecutor executor) {
        this.executor = executor;
    }

    @Bean("batchContainerFactory")
    public RabbitListenerContainerFactory<?> batchContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO); // Enable manual acknowledgement
        factory.setBatchListener(true);
        factory.setConsumerBatchEnabled(true);
        factory.setTaskExecutor(executor);
        factory.setConcurrentConsumers(concurrency);
        factory.setBatchSize(batchSize);
        return factory;
    }
}
