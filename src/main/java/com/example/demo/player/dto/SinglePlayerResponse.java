package com.example.demo.player.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SinglePlayerResponse {
    private Long playerId;
    private String firstName;
    private String lastName;
    private String position;
    private int numberOfGoals;
    private int numberOfAssists;
    private Long teamId;
    private String exactPosition;
    private int shirtNumber;
}
