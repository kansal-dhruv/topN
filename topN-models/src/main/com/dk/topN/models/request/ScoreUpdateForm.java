package com.dk.topN.models.request;

import lombok.Data;

@Data
public class ScoreUpdateForm {
    private int score;
    private String playerId;
}
