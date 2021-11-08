package com.example.demo.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class NewEventDto {
    private Long gameId;
    private Long leagueId;
    private Long eventId;
}
