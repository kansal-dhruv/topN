package com.dk.topN.aggregator.service.impl;

import com.dk.topN.aggregator.service.HashService;
import com.dk.topN.models.request.ScoreUpdateForm;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayerIdHashService implements HashService<ScoreUpdateForm, Integer> {

    @Override
    public Integer generateHash(ScoreUpdateForm input) {
        int hash = 0;
        for (char c : input.getPlayerId().toCharArray()) {
            hash += c;
        }
        return Math.abs(hash) % 5;
    }

    @Override
    public Map<Integer, List<ScoreUpdateForm>> generateHashMap(List<ScoreUpdateForm> data) {
        Map<Integer, List<ScoreUpdateForm>> map = new HashMap<>();
        if(CollectionUtils.isEmpty(data)) {
            return map;
        }
        for (ScoreUpdateForm datum : data) {
            Integer hash = generateHash(datum);
            List<ScoreUpdateForm> hashedData = map.getOrDefault(hash, new ArrayList<ScoreUpdateForm>());
            hashedData.add(datum);
            map.put(hash, hashedData);
        }
        return map;
    }

}
