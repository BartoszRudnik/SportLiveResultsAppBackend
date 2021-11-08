package com.example.demo.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddGameRequest {
    private Long teamAId;
    private Long teamBId;
    private Long leagueId;
    private LocalDateTime gameStartDate;
    private String gameStatus;
    private int round;
}
