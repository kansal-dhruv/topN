package com.dk.topN.aggregator.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface HashService <I, R>{
    R generateHash(I input);
}
