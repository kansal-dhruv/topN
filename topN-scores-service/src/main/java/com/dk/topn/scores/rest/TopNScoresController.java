package com.dk.topn.scores.rest;

import com.dk.topN.models.request.UpdateScoreDto;
import com.dk.topn.scores.service.TopNProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TopNScoresController {

    @Autowired
    private TopNProcessorService topNProcessorService;

    @GetMapping("/top")
    public List<UpdateScoreDto> getTopScores(){
        return topNProcessorService.getCurrentData();
    }
}
