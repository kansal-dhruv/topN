package com.dk.topn.processor.service;

import java.util.List;

public interface PostProcessService<I> {
    public void postProcess(List<I> data);
}
