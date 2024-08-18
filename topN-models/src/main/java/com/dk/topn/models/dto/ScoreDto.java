package com.dk.topn.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class ScoreDto implements Serializable {
    @NotNull
    private Double score = 0.0;
    @NotBlank
    private String playerId;
}
