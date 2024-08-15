package com.dk.topn.processor.service;

import java.util.List;

public interface ProcessorService<I> extends ProcessService<I>, PostProcessService<I> {
    public default void process(List<I> input){
        processData(input);
        postProcess(input);
    }
    public List<I> getCurrentData();
}
