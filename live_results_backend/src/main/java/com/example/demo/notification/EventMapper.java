package com.example.demo.notification;

import com.example.demo.notification.dto.DeleteGameEventRequest;
import com.example.demo.notification.dto.NewEventDto;
import com.example.demo.notification.dto.NewTimeEventDto;
import com.example.demo.notification.dto.UpdateGameEventRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import wiremock.org.apache.commons.lang3.RandomStringUtils;

@Component
@AllArgsConstructor
public class EventMapper {
    public SseEmitter.SseEventBuilder toSseEventBuilder(UpdateGameEventRequest event){
        return SseEmitter.event()
                .id(RandomStringUtils.randomAlphanumeric(12))
                .name("update " + event.getGameId() + " " + event.getLeagueId() + " " + event.getEventId());
    }

    public SseEmitter.SseEventBuilder toSseEventBuilder(DeleteGameEventRequest event){
        return SseEmitter.event()
                .id(RandomStringUtils.randomAlphanumeric(12))
                .name("delete " + event.getGameId() + " " + event.getLeagueId() + " " + event.getEventId());
    }

    public SseEmitter.SseEventBuilder toSseEventBuilder(Long gameId){
        return SseEmitter.event()
                .id(RandomStringUtils.randomAlphanumeric(12))
                .name(Long.toString(gameId));
    }

    public SseEmitter.SseEventBuilder toSseEventBuilder(NewEventDto event) {
        return SseEmitter.event()
                .id(RandomStringUtils.randomAlphanumeric(12))
                .name(event.getGameId() + " " + event.getLeagueId() + " " + event.getEventId());
    }

    public SseEmitter.SseEventBuilder toSseEventBuilder(NewTimeEventDto event){
        return SseEmitter.event()
                .id(RandomStringUtils.randomAlphanumeric(12))
                .name("timeEvent" + " " + event.getGameId() + " " + event.getLeagueId() + " " + event.getEventType());
    }
}
