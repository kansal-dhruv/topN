package com.dk.topn.processor.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface ProcessService<I> {
    void processData(@NotNull @Valid List<I> data);
}
