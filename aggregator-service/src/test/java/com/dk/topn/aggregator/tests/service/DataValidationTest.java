package com.dk.topn.aggregator.tests.service;

import com.dk.topn.aggregator.listeners.RmqListeners;
import com.dk.topn.aggregator.service.impl.RabbitMqDataPipelineService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.dk.topn.models.dto.ScoreDto;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Comparator;
import java.util.List;

@SpringBootTest
public class DataValidationTest {

    @MockBean
    RmqListeners rmqListeners;

    @MockBean
    RabbitTemplate rabbitTemplate;

    @MockBean
    ConnectionFactory connectionFactory;

    @Autowired
    RabbitMqDataPipelineService rabbitMqDataPipelineService;

    @Test
    public void test_with_null(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            try {
                rabbitMqDataPipelineService.executePipeline(null);
            } catch (ConstraintViolationException e) {
                Assertions.assertEquals("executePipeline.data: must not be null", e.getMessage());
                throw e;
            }
        });

    }

    @Test
    public void test_with_invalidPlayerId(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            try {
                ScoreDto scoreDto = new ScoreDto();
                scoreDto.setPlayerId(null);
                scoreDto.setScore(11.00);
                rabbitMqDataPipelineService.executePipeline(List.of(scoreDto));
            } catch (ConstraintViolationException e) {
                Assertions.assertEquals("executePipeline.data[0].playerId: must not be blank", e.getMessage());
                throw e;
            }
        });
    }

    @Test
    public void test_with_invalidScore(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            try {
                ScoreDto scoreDto = new ScoreDto();
                scoreDto.setPlayerId("playerId");
                scoreDto.setScore(null);
                rabbitMqDataPipelineService.executePipeline(List.of(scoreDto));
            } catch (ConstraintViolationException e) {
                Assertions.assertEquals("executePipeline.data[0].score: must not be null", e.getMessage());
                throw e;
            }
        });
    }

    @Test
    public void test_with_invalidPlayerId_Score(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            try {
                ScoreDto scoreDto = new ScoreDto();
                scoreDto.setPlayerId(null);
                scoreDto.setScore(null);
                rabbitMqDataPipelineService.executePipeline(List.of(scoreDto));
            } catch (ConstraintViolationException e) {
                List<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations().stream().sorted(Comparator.comparing(x->x.getPropertyPath().toString())).toList();
                Assertions.assertEquals("executePipeline.data[0].playerId", constraintViolations.get(0).getPropertyPath().toString());
                Assertions.assertEquals("executePipeline.data[0].score", constraintViolations.get(1).getPropertyPath().toString());
                Assertions.assertEquals("must not be blank", constraintViolations.get(0).getMessage());
                Assertions.assertEquals("must not be null", constraintViolations.get(1).getMessage());
                throw e;
            }
        });
    }

}
