package com.dk.topn.processor.service;

import java.util.List;

public interface ProcessService<I> {
    void processData(List<I> data);
}
