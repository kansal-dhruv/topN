package com.dk.topn.scores.service;

import com.dk.topN.models.request.UpdateScoreDto;
import com.dk.topn.processor.service.impl.HeapProcessorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TopNProcessorService extends HeapProcessorService {
    @Override
    public void postProcess(List<UpdateScoreDto> data) {
        //TODO interact with DB
        log.info("Max Heap after new Size: {}, Message : [{}]", maxHeap.size(), maxHeap.stream().map(UpdateScoreDto::toString).collect(Collectors.joining(",")));
    }
}
