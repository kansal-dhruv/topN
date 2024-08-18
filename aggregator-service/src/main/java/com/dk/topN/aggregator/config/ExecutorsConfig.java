package com.dk.topn.aggregator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;

@Configuration
public class ExecutorsConfig {

    @Bean("virtualThreadExecutor")
    public TaskExecutor getExecutor() {
        return new VirtualThreadTaskExecutor();
    }
}
