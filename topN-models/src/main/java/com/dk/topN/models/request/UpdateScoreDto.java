package com.dk.topN.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UpdateScoreDto implements Serializable {
    @NotNull
    private Double score = 0.0;
    @NotBlank
    private String playerId;
}
