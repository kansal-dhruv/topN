package com.dk.topn.processor.service;


import com.dk.topn.models.dto.ScoreDto;
import com.dk.topn.processor.listener.RmqListeners;
import com.dk.topn.processor.service.impl.HeapProcessorService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.context.SpringRabbitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
@Log4j2
@SpringRabbitTest
@TestPropertySource(properties = {
        "max.heap.size=1000",
})
public class HeapSize1000Test {

    @Autowired
    private HeapProcessorService heapProcessorService;

    @MockBean
    RmqListeners rmqListeners;

    @MockBean
    RabbitTemplate rabbitTemplate;

    @MockBean
    ConnectionFactory connectionFactory;

    private final int MAX_SCORE_VALUE = 10;

    @Test()
    public void validateHeapSize(){
        heapProcessorService.process(sampleData(10000, 0));
            List<ScoreDto> heap = heapProcessorService.getCurrentData();
            Assertions.assertEquals(1000, heap.size());
    }

    @Test()
    public void validateHeapSize_2(){
        heapProcessorService.process(sampleData(100000, 0));
        List<ScoreDto> heap = heapProcessorService.getCurrentData();
        Assertions.assertEquals(1000, heap.size());
    }

    @Test()
    public void validateHeapSize_3(){
        heapProcessorService.process(sampleData(1000000, 0));
        List<ScoreDto> heap = heapProcessorService.getCurrentData();
        Assertions.assertEquals(1000, heap.size());
    }

    @Test()
    public void validateMaxHeap(){
        heapProcessorService.process(sampleData(10000, 1000));
        List<ScoreDto> heap = heapProcessorService.getCurrentData();
        for (int i = 0; i < heap.size(); i++) {
            Assertions.assertTrue(heap.get(i).getScore() >= MAX_SCORE_VALUE);
        }
    }

    private List<ScoreDto> sampleData(int size, int countOfGreaterThanMax){
        Random rand = new Random();
        List<ScoreDto> data = new ArrayList<>();
        for(int i =1;i <= size;i++,countOfGreaterThanMax--){
            ScoreDto ScoreDto = new ScoreDto();
            ScoreDto.setPlayerId("player" + rand.nextInt(1000));
            ScoreDto.setScore(rand.nextDouble(MAX_SCORE_VALUE));
            if(countOfGreaterThanMax > 0){
                ScoreDto.setScore(ScoreDto.getScore() + MAX_SCORE_VALUE);
            }
            data.add(ScoreDto);
        }
        return data;
    }

}
