package com.dk.topn.aggregator.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Validated
public interface MappedHashService<I, R> extends HashService<I, R> {
    Map<R, List<I>> generateHashMap(@Valid @NotNull List<I> data);
}
