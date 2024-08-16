package com.dk.topN.aggregator.service.impl;

import com.dk.topN.aggregator.service.DataPipelineService;
import com.dk.topN.models.request.UpdateScoreDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.dk.topN.util.JsonUtil.covertFormToByteArray;

@Service
@Log4j2
public class RabbitMqDataPipelineService implements DataPipelineService<UpdateScoreDto> {

    @Autowired
    PlayerIdHashService hashService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void executePipeline(List<UpdateScoreDto> data) {
        Map<Integer, List<UpdateScoreDto>> dataGroupedByHash = hashService.generateHashMap(data);
        dataGroupedByHash.forEach((hash, scoreList)->{
            for (UpdateScoreDto updateScoreDto : scoreList) {
                rabbitTemplate.send(hash.toString(), new Message(covertFormToByteArray(updateScoreDto)));
            }
        });
    }
}
