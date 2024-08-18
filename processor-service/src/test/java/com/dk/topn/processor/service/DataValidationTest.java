package com.dk.topn.processor.service;


import com.dk.topn.models.dto.ScoreDto;
import com.dk.topn.processor.listener.RmqListeners;
import com.dk.topn.processor.service.impl.HeapProcessorService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.context.SpringRabbitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Comparator;
import java.util.List;

@SpringBootTest
@Log4j2
@SpringRabbitTest
public class DataValidationTest {

    @Autowired
    private HeapProcessorService heapProcessorService;

    @MockBean
    RmqListeners rmqListeners;

    @MockBean
    RabbitTemplate rabbitTemplate;

    @MockBean
    ConnectionFactory connectionFactory;

    @Test
    public void test_with_null(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            try {
                heapProcessorService.process(null);
            } catch (ConstraintViolationException e) {
                Assertions.assertEquals("process.input: must not be null", e.getMessage());
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
                heapProcessorService.process(List.of(scoreDto));
            } catch (ConstraintViolationException e) {
                Assertions.assertEquals("process.input[0].playerId: must not be blank", e.getMessage());
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
                heapProcessorService.process(List.of(scoreDto));
            } catch (ConstraintViolationException e) {
                Assertions.assertEquals("process.input[0].score: must not be null", e.getMessage());
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
                heapProcessorService.process(List.of(scoreDto));
            } catch (ConstraintViolationException e) {
                List<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations().stream().sorted(Comparator.comparing(x->x.getPropertyPath().toString())).toList();
                Assertions.assertEquals("process.input[0].playerId", constraintViolations.get(0).getPropertyPath().toString());
                Assertions.assertEquals("process.input[0].score", constraintViolations.get(1).getPropertyPath().toString());
                Assertions.assertEquals("must not be blank", constraintViolations.get(0).getMessage());
                Assertions.assertEquals("must not be null", constraintViolations.get(1).getMessage());
                throw e;
            }
        });
    }
}
