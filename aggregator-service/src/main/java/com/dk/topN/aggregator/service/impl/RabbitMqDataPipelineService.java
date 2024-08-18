package com.dk.topn.aggregator.service.impl;

import com.dk.topn.aggregator.service.DataPipelineService;
import com.dk.topn.models.dto.ScoreDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.dk.topn.util.JsonUtil.covertFormToByteArray;

@Service
@Log4j2
public class RabbitMqDataPipelineService implements DataPipelineService<ScoreDto> {

    @Autowired
    PlayerIdHashService hashService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void executePipeline(List<ScoreDto> data) {
        Map<Integer, List<ScoreDto>> dataGroupedByHash = hashService.generateHashMap(data);
        dataGroupedByHash.forEach((hash, scoreList)->{
            for (ScoreDto scoreDto : scoreList) {
                rabbitTemplate.send(hash.toString(), new Message(covertFormToByteArray(scoreDto)));
            }
        });
    }
}
