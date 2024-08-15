package com.dk.topn.processor.listener;

import com.dk.topN.models.request.UpdateScoreDto;
import com.dk.topn.processor.service.impl.HeapProcessorService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.dk.topN.util.JsonUtil.covertFormToByteArray;

@Component
public class ListenerConfig {

    @Autowired
    private HeapProcessorService heapProcessorService;

    @RabbitListener(
            id = "shardListener-1",
            queuesToDeclare = {
                    @Queue(name = "0")
            },
            containerFactory = "batchContainerFactory"
    )
    private void scoreListener(List<Message> scoreMessages){
        List<UpdateScoreDto> updateScoreDtos = new ArrayList<>();
        for (Message scoreMessage : scoreMessages) {
            updateScoreDtos.add(covertFormToByteArray(scoreMessage.getBody(), UpdateScoreDto.class));
        }
        heapProcessorService.process(updateScoreDtos);
    }
}