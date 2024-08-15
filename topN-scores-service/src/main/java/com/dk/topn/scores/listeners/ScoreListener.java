package com.dk.topn.scores.listeners;

import com.dk.topN.models.request.UpdateScoreDto;
import com.dk.topn.scores.service.TopNProcessorService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.dk.topN.util.JsonUtil.covertFormToByteArray;


@Service
public class ScoreListener {

    @Autowired
    TopNProcessorService topNProcessorService;

    @RabbitListener(
            id = "mainListener",
            queuesToDeclare = {
                    @Queue(name = "processed-data-queue")
            },
            containerFactory = "batchContainerFactory"
    )
    private void scoreListener(List<Message> scoreMessages){
        List<UpdateScoreDto> updateScoreDtos = new ArrayList<>();
        for (Message scoreMessage : scoreMessages) {
            updateScoreDtos.add(covertFormToByteArray(scoreMessage.getBody(), UpdateScoreDto.class));
        }
        topNProcessorService.process(updateScoreDtos);
    }
}
