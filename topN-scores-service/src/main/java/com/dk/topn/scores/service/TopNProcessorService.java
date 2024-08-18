package com.dk.topn.scores.service;

import com.dk.topn.models.dto.ScoreDto;
import com.dk.topn.processor.service.impl.HeapProcessorService;
import com.dk.topn.scores.entity.IdProjection;
import com.dk.topn.scores.repo.TopScoreRepo;
import com.dk.topn.scores.entity.TopScores;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class TopNProcessorService extends HeapProcessorService {

    @Autowired
    private TopScoreRepo topScoreRepo;

    @Override
    @Transactional
    public void process(List<ScoreDto> data) {
        super.process(data);
    }

    @Override
    public void postProcess(List<ScoreDto> data) {
        topScoreRepo.saveAll(convertDtoToEntity(data));
        List<Long> idsToKeep = topScoreRepo.getTopKids(maxHeapSize).stream().map(IdProjection::getId).toList();
        topScoreRepo.deleteByIdNotIn(idsToKeep);
    }

    @Override
    public List<ScoreDto> getCurrentData() {
        return topScoreRepo.findAllByOrderByScoreDesc(Pageable.ofSize(maxHeapSize))
                .stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    private ScoreDto convertEntityToDto(TopScores topScores) {
        ScoreDto ScoreDto = new ScoreDto();
        ScoreDto.setScore(topScores.getScore());
        ScoreDto.setPlayerId(topScores.getPlayerId());
        return ScoreDto;

    }

    private List<TopScores> convertDtoToEntity(List<ScoreDto> dtos) {
        if(CollectionUtils.isEmpty(dtos)){
            return new ArrayList<>();
        }
        List<TopScores> entities = new ArrayList<>();
        for (ScoreDto dto : dtos) {
            TopScores entity = new TopScores();
            entity.setScore(dto.getScore());
            entity.setPlayerId(dto.getPlayerId());
            entities.add(entity);
        }
        return entities;
    }
}
