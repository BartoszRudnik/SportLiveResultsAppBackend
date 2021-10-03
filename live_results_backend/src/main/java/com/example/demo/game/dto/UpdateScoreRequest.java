package com.example.demo.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateScoreRequest {
    private int scoreTeamA;
    private int scoreTeamB;
}
