package com.example.demo.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SinglePlayerRequest {
    private Long playerId;
    private String playerStatus;
}
