package com.dk.topn.processor.service.impl;

import com.dk.topN.models.request.UpdateScoreDto;
import com.dk.topn.processor.service.ProcessorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static com.dk.topN.util.JsonUtil.covertFormToByteArray;

@Service
@Log4j2
public class HeapProcessorService implements ProcessorService<UpdateScoreDto> {

    @Value("${max.heap.size:1000}")
    protected int maxHeapSize;

    @Autowired
    RabbitTemplate rabbitTemplate;

    Comparator<UpdateScoreDto> scoreComparator = new Comparator<UpdateScoreDto>() {
        @Override
        public int compare(UpdateScoreDto o1, UpdateScoreDto o2) {
            return Double.compare(o1.getScore(), o2.getScore());
        }
    };

    protected PriorityQueue<UpdateScoreDto> maxHeap = new PriorityQueue<>(scoreComparator);

    @Override
    public void processData(List<UpdateScoreDto> data) {
        log.info("recieved a batch of size {}", data.size());
        for (UpdateScoreDto datum : data) {
            maxHeap.add(datum);
            while(maxHeap.size() > maxHeapSize) {
                UpdateScoreDto removedScore = maxHeap.poll();
//                log.info("Removed Score: {}", removedScore.getScore());
            }
        }
    }

    @Override
    public List<UpdateScoreDto> getCurrentData() {
        return maxHeap.stream().toList();
    }

    @Override
    public void postProcess(List<UpdateScoreDto> data) {
        int msgcount = 0;
        for (UpdateScoreDto updateScoreDto : maxHeap) {
            rabbitTemplate.send("processed-data-queue", new Message(covertFormToByteArray(updateScoreDto)));
            msgcount++;
        }
        log.info("processed {} messages", msgcount);
    }
}
