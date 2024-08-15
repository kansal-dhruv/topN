package com.dk.topN.aggregator.service;

import java.util.List;
import java.util.Map;

public interface MappedHashService<I, R> extends HashService<I, R> {
    Map<R, List<I>> generateHashMap(List<I> data);
}
