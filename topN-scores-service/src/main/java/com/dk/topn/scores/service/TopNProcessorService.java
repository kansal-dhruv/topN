package com.dk.topn.scores.service;

import com.dk.topN.models.request.UpdateScoreDto;
import com.dk.topn.processor.service.impl.HeapProcessorService;
import com.dk.topn.scores.TopScoreRepo;
import com.dk.topn.scores.entity.TopScores;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class TopNProcessorService extends HeapProcessorService {

    @Autowired
    private TopScoreRepo topScoreRepo;

    @Override
    @Transactional
    public void process(List<UpdateScoreDto> data) {
        super.process(data);
    }

    @Override
    public void postProcess(List<UpdateScoreDto> data) {
        topScoreRepo.saveAll(convertDtoToEntity(data));
        topScoreRepo.deleteUnderKValues(maxHeapSize*2, maxHeapSize);
    }

    @Override
    public List<UpdateScoreDto> getCurrentData() {
        return topScoreRepo
                .findAll(Pageable.ofSize(maxHeapSize))
                .get()
                .map(this::convertEntityToDto)
                .toList();
    }

    private UpdateScoreDto convertEntityToDto(TopScores topScores) {
        UpdateScoreDto UpdateScoreDto = new UpdateScoreDto();
        UpdateScoreDto.setScore(topScores.getScore());
        UpdateScoreDto.setPlayerId(topScores.getPlayerId());
        return UpdateScoreDto;

    }

    private List<TopScores> convertDtoToEntity(List<UpdateScoreDto> dtos) {
        if(CollectionUtils.isEmpty(dtos)){
            return new ArrayList<>();
        }
        List<TopScores> entities = new ArrayList<>();
        for (UpdateScoreDto dto : dtos) {
            TopScores entity = new TopScores();
            entity.setScore(dto.getScore());
            entity.setPlayerId(dto.getPlayerId());
            entities.add(entity);
        }
        return entities;
    }
}
