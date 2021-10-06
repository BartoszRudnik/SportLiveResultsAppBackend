package com.example.demo.league.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameEventsResponse {
    private int gameMinute;
    private Long playerId;
    private String eventType;
}
