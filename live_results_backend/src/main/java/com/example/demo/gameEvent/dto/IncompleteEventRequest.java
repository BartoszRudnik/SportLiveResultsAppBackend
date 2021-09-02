package com.example.demo.gameEvent.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class IncompleteEventRequest {

    private int minute;
    private String eventType;
    private Long teamId;
    private Long gameId;

}
