package com.dk.topn.processor.service;


import com.dk.topN.models.request.UpdateScoreDto;
import com.dk.topn.processor.service.impl.HeapProcessorService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootTest
@Log4j2
public class HeapProcessorServiceTest {

    @Autowired
    private HeapProcessorService heapProcessorService;

    @Test
    public void test_1000data_100heapsize(){
        for(int i =0;i<=1000;i++) {
            heapProcessorService.process(sampleData());
            List<UpdateScoreDto> heap = heapProcessorService.getCurrentData();
            log.info("Max Heap after new Size:{}, Message : [{}]", heap.size(), heap.stream().map(UpdateScoreDto::toString).collect(Collectors.joining(",")));
        }
    }

    private List<UpdateScoreDto> sampleData(){
        Random rand = new Random();
        List<UpdateScoreDto> data = new ArrayList<>();
        for(int i =1;i <= 1000;i++){
            UpdateScoreDto updateScoreDto = new UpdateScoreDto();
            updateScoreDto.setPlayerId("player" + rand.nextInt(1000));
            updateScoreDto.setScore(rand.nextDouble(10.00));
            data.add(updateScoreDto);
        }
        return data;
    }
}
