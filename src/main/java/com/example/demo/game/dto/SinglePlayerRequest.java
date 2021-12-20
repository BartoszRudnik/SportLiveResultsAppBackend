package com.example.demo.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class SinglePlayerRequest {
    private Long playerId;
    private String playerStatus;
    private String playerPosition;
}
