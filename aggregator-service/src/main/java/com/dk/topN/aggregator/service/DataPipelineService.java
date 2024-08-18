package com.dk.topn.aggregator.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface DataPipelineService<T> {
    void executePipeline(@Valid @NotNull List<T> data);
}
