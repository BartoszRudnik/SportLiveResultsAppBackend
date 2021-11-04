package com.example.demo.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class DeleteGameEventRequest {
    private Long gameId;
    private Long leagueId;
    private Long eventId;
}
