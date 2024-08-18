package com.dk.topn.processor.listener;

import com.dk.topn.models.dto.ScoreDto;
import com.dk.topn.processor.service.impl.HeapProcessorService;
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
    private HeapProcessorService heapProcessorService;

    @RabbitListener(
            queuesToDeclare = {
                    @Queue(name = "${consumer.queue.name:0}")
            },
            containerFactory = "batchContainerFactory"
    )
    private void scoreListener(List<Message> scoreMessages){
        List<ScoreDto> scoreDtos = new ArrayList<>();
        for (Message scoreMessage : scoreMessages) {
            scoreDtos.add(covertFormToByteArray(scoreMessage.getBody(), ScoreDto.class));
        }
        heapProcessorService.process(scoreDtos);
    }
}