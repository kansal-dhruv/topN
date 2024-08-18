package com.dk.topn.aggregator.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Validated
public interface HashService <I, R>{
    R generateHash(@Valid @NotNull I input);
}
