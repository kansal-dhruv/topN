package com.dk.topN.aggregator.listeners;

import com.dk.topN.aggregator.service.impl.RabbitMqDataPipelineService;
import com.dk.topN.models.request.ScoreUpdateForm;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.dk.topN.aggregator.util.JsonUtil.covertFormToByteArray;

@Component
public class ListenerConfig {

    @Autowired
    private RabbitMqDataPipelineService rabbitMqDataPipelineService;

    @RabbitListener(
            id = "mainListener",
            queuesToDeclare = {
                    @Queue(name = "scores-queue")
            },
            containerFactory = "batchContainerFactory"
    )
    private void scoreListener(List<Message> scoreMessages){
        List<ScoreUpdateForm> scoreUpdateForms = new ArrayList<>();
        for (Message scoreMessage : scoreMessages) {
            scoreUpdateForms.add(covertFormToByteArray(scoreMessage.getBody(), ScoreUpdateForm.class));
        }
        rabbitMqDataPipelineService.executePipeline(scoreUpdateForms);
    }
}
