package com.example.demo.league.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameEventsResponse {
    private Long gameMinute;
    private Long playerId;
    private Long teamId;
    private String eventType;
}
