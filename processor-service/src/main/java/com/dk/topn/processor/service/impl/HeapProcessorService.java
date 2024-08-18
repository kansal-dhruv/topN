package com.dk.topn.processor.service.impl;

import com.dk.topn.models.dto.ScoreDto;
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

import static com.dk.topn.util.JsonUtil.covertFormToByteArray;

@Service
@Log4j2
public class HeapProcessorService implements ProcessorService<ScoreDto> {

    @Value("${max.heap.size:1000}")
    protected int maxHeapSize;

    @Value("${topn.processor.queue:top-scores-queue}")
    private String topNProcessorQueue;

    @Autowired
    RabbitTemplate rabbitTemplate;

    Comparator<ScoreDto> scoreComparator = new Comparator<ScoreDto>() {
        @Override
        public int compare(ScoreDto o1, ScoreDto o2) {
            return Double.compare(o1.getScore(), o2.getScore());
        }
    };

    protected PriorityQueue<ScoreDto> maxHeap = new PriorityQueue<>(scoreComparator);

    @Override
    public void processData(List<ScoreDto> data) {
        for (ScoreDto datum : data) {
            maxHeap.add(datum);
            while(maxHeap.size() > maxHeapSize) {
                ScoreDto removedScore = maxHeap.poll();
                log.debug("Removed Score: {}", removedScore.getScore());
            }
        }
    }

    @Override
    public List<ScoreDto> getCurrentData() {
        return maxHeap.stream().toList();
    }

    @Override
    public void postProcess(List<ScoreDto> data) {
        for (ScoreDto scoreDto : maxHeap) {
            rabbitTemplate.send(topNProcessorQueue, new Message(covertFormToByteArray(scoreDto)));
        }
    }
}