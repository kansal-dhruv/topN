package com.dk.topn.aggregator.tests.service;


import com.dk.topn.aggregator.listeners.RmqListeners;
import com.dk.topn.aggregator.service.impl.PlayerIdHashService;
import com.dk.topn.models.dto.ScoreDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Random;

@SpringBootTest
@Log4j2
@TestPropertySource(
        properties = {
                "parallel.queue.workers=10",
        })
public class MaxQueueWorkers10Test {

    @Autowired
    private PlayerIdHashService playerIdHashService;

    @MockBean
    RmqListeners rmqListeners;

    @MockBean
    RabbitTemplate rabbitTemplate;

    @MockBean
    ConnectionFactory connectionFactory;

    private final int MAX_SCORE_VALUE = 10;

    @Test()
    public void validateHeapSize() {
        Random rand = new Random();
        for (int i = 1; i < 10; i++) {
            ScoreDto scoreDto = new ScoreDto();
            scoreDto.setPlayerId(String.valueOf(i));
            scoreDto.setScore(rand.nextDouble(MAX_SCORE_VALUE));
            int hashValue = playerIdHashService.generateHash(scoreDto);
            Assertions.assertEquals((i + 8) % 10, hashValue);
        }

    }
}