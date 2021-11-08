package com.example.demo.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UpdateGameEventRequest {
    private Long leagueId;
    private Long gameId;
    private Long eventId;
}
