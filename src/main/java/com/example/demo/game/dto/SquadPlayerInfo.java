package com.example.demo.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SquadPlayerInfo {
    private Long playerId;
    private int lastShirtNumber;
    private String lastPositionName;
}
