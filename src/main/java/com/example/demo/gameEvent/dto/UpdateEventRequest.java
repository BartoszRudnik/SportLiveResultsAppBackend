package com.example.demo.gameEvent.dto;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UpdateEventRequest {

    private Long eventId;
    private int minute;
    private String eventType;
    private Long teamId;
    private Long gameId;
    private Long playerId;

}
