package com.dk.topN.aggregator.service.impl;

import com.dk.topN.aggregator.service.MappedHashService;
import com.dk.topN.models.request.UpdateScoreDto;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayerIdHashService implements MappedHashService<UpdateScoreDto, Integer> {

    @Override
    public Integer generateHash(UpdateScoreDto input) {
        //TODO revisit
        int hash = 0;
        for (char c : input.getPlayerId().toCharArray()) {
            hash += c;
        }
        return Math.abs(hash) % 5;
    }

    @Override
    public Map<Integer, List<UpdateScoreDto>> generateHashMap(List<UpdateScoreDto> data) {
        Map<Integer, List<UpdateScoreDto>> map = new HashMap<>();
        if(CollectionUtils.isEmpty(data)) {
            return map;
        }
        for (UpdateScoreDto datum : data) {
            Integer hash = generateHash(datum);
            List<UpdateScoreDto> hashedData = map.getOrDefault(hash, new ArrayList<UpdateScoreDto>());
            hashedData.add(datum);
            map.put(hash, hashedData);
        }
        return map;
    }

}
