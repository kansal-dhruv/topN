package com.dk.topn.aggregator.listeners;

import com.dk.topn.aggregator.service.impl.RabbitMqDataPipelineService;
import com.dk.topn.models.dto.ScoreDto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.dk.topn.util.JsonUtil.covertFormToByteArray;

@Component
public class RmqListeners {

    @Autowired
    private RabbitMqDataPipelineService rabbitMqDataPipelineService;

    @RabbitListener(
            id = "mainListener",
            queuesToDeclare = {
                    @Queue(name = "${data.influx.queue:scores-queue}")
            },
            containerFactory = "batchContainerFactory"
    )
    private void scoreListener(List<Message> scoreMessages){
        List<ScoreDto> scoreDtos = new ArrayList<>();
        for (Message scoreMessage : scoreMessages) {
            scoreDtos.add(covertFormToByteArray(scoreMessage.getBody(), ScoreDto.class));
        }
        rabbitMqDataPipelineService.executePipeline(scoreDtos);
    }
}