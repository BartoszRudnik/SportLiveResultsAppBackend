package com.example.demo.player.dto;

import com.example.demo.gamePlayer.GamePlayerPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GamePlayerResponse {
    private Long playerId;
    private String position;
}
