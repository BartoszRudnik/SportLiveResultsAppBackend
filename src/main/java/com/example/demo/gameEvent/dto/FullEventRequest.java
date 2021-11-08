package com.example.demo.gameEvent.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class FullEventRequest {
    private String eventType;
    private Long teamId;
    private Long gameId;
    private Long playerId;
}
