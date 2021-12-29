package com.example.demo.player.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SinglePlayerStatistics {
    private int gamesPlayed;
    private int firstSquadGames;
    private int minutes;
    private int goals;
    private int assists;
    private int yellowCards;
    private int redCards;
    private int teamGames;
    private int teamGoals;
}
