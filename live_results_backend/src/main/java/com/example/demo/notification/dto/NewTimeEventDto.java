package com.example.demo.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class NewTimeEventDto {
    private Long gameId;
    private Long leagueId;
    private String eventType;
}
