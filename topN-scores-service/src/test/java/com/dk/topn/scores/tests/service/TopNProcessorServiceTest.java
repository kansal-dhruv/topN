package com.dk.topn.scores.tests.service;

import com.dk.topn.models.dto.ScoreDto;
import com.dk.topn.processor.listener.RmqListeners;
import com.dk.topn.scores.service.TopNProcessorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TopNProcessorServiceTest {

    @Autowired
    private TopNProcessorService topNProcessorService;

    @MockBean
    RmqListeners rmqListeners;

    @MockBean
    RabbitTemplate rabbitTemplate;

    @MockBean
    ConnectionFactory connectionFactory;

    @Test
    public void test1(){
        List<ScoreDto> scores = sampleData();
        topNProcessorService.process(scores);
        List<ScoreDto> topKinput = scores.stream().sorted(Comparator.comparing(ScoreDto::getScore).reversed()).limit(1000).toList();
        List<ScoreDto> topScoresFromService = topNProcessorService.getCurrentData();
        for (int i = 0; i < 1000; i++) {
            Assertions.assertEquals(topKinput.get(i).getPlayerId(), topScoresFromService.get(i).getPlayerId());
            Assertions.assertEquals(topKinput.get(i).getScore(), topScoresFromService.get(i).getScore());
        }

    }

    private List<ScoreDto> sampleData(){
        Random rand = new Random();
        List<ScoreDto> data = new ArrayList<>();
        for(int i =1;i <= 1000;i++){
            ScoreDto scoreDto = new ScoreDto();
            scoreDto.setPlayerId("player" + rand.nextInt(1000));
            scoreDto.setScore(rand.nextDouble(10.00));
            data.add(scoreDto);
        }
        return data;
    }

}
