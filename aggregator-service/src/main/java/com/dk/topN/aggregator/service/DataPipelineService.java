package com.dk.topN.aggregator.service;

import java.util.List;

public interface DataPipelineService<T> {
    void executePipeline(List<T> data);
}
