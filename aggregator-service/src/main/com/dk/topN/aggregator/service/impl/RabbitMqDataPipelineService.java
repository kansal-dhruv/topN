package com.dk.topN.aggregator.service.impl;

import com.dk.topN.aggregator.service.DataPipelineService;
import com.dk.topN.aggregator.service.HashService;
import com.dk.topN.models.request.ScoreUpdateForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dk.topN.aggregator.util.JsonUtil.covertFormToByteArray;

@Service
@Log4j2
public class RabbitMqDataPipelineService implements DataPipelineService<ScoreUpdateForm> {

    @Autowired
    HashService<ScoreUpdateForm, Integer> hashService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void executePipeline(List<ScoreUpdateForm> data) {
        Map<Integer, List<ScoreUpdateForm>> dataGroupedByHash = hashService.generateHashMap(data);
        dataGroupedByHash.forEach((hash, scoreList)->{
            for (ScoreUpdateForm scoreUpdateForm : scoreList) {
                rabbitTemplate.send(hash.toString(), new Message(covertFormToByteArray(scoreUpdateForm)));
            }

        });
    }
}
