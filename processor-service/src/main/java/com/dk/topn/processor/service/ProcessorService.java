package com.dk.topn.processor.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface ProcessorService<I> extends ProcessService<I>, PostProcessService<I> {
    public default void process(@NotNull @Valid List<I> input){
        processData(input);
        postProcess(input);
    }
    public List<I> getCurrentData();
}
