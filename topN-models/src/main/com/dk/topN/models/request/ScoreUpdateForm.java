package com.dk.topN.models.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScoreUpdateForm {
    private int score;
    private String playerId;
}
