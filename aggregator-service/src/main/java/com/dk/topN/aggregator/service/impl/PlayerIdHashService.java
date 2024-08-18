package com.dk.topn.aggregator.service.impl;

import com.dk.topn.aggregator.service.MappedHashService;
import com.dk.topn.models.dto.ScoreDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayerIdHashService implements MappedHashService<ScoreDto, Integer> {

    @Value("${parallel.queue.workers:1}")
    private int parallelQueueWorkers;

    @Override
    public Integer generateHash(ScoreDto input) {
        //TODO revisit
        int hash = 0;
        for (char c : input.getPlayerId().toCharArray()) {
            hash += c;
        }
        return Math.abs(hash) % parallelQueueWorkers;
    }

    @Override
    public Map<Integer, List<ScoreDto>> generateHashMap(List<ScoreDto> data) {
        Map<Integer, List<ScoreDto>> map = new HashMap<>();
        if(CollectionUtils.isEmpty(data)) {
            return map;
        }
        for (ScoreDto datum : data) {
            Integer hash = generateHash(datum);
            List<ScoreDto> hashedData = map.getOrDefault(hash, new ArrayList<ScoreDto>());
            hashedData.add(datum);
            map.put(hash, hashedData);
        }
        return map;
    }

}
