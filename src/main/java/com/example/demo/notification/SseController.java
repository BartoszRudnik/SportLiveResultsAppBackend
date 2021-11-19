package com.example.demo.notification;

import com.example.demo.notification.dto.DeleteGameEventRequest;
import com.example.demo.notification.dto.NewEventDto;
import com.example.demo.notification.dto.NewTimeEventDto;
import com.example.demo.notification.dto.UpdateGameEventRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping(path = "api/v1/notification")
@AllArgsConstructor
public class SseController {
    private final EmitterService emitterService;
    private final NotificationService notificationService;

    @PostMapping("/publishUpdateGameEvent")
    public void updateGameEvent(@RequestBody UpdateGameEventRequest request){
        this.notificationService.sendNotification(request);
    }

    @PostMapping("/publishDeleteGameEvent")
    public void deleteGameEvent(@RequestBody DeleteGameEventRequest request){
        this.notificationService.sendNotification(request);
    }

    @PostMapping("/publishNewLineup/{gameId}")
    public void publishNewLineup(@PathVariable Long gameId){ this.notificationService.sendNotification(gameId, "subtitle");}

    @PostMapping("/publishNewStatistic/{gameId}")
    public void publishNewStatistic(@PathVariable Long gameId){
        this.notificationService.sendNotification(gameId);
    }

    @PostMapping("/publishNewEvent")
    public void publishNewEvent(@RequestBody NewEventDto request){
        this.notificationService.sendNotification(request);
    }

    @PostMapping("/publishNewTimeEvent")
    public void publishNewTimeEvent(@RequestBody NewTimeEventDto request){
        this.notificationService.sendNotification(request);
    }

    @PostMapping("/publishNewReporterEvent/{gameId}/{leagueId}")
    public void publishNewReporterEvent(@PathVariable Long gameId, @PathVariable Long leagueId){
        this.notificationService.sendNotification(gameId, leagueId);
    }

    @PostMapping("/publishNewMessageEvent/{gameId}/{messageId}/{type}")
    public void publishNewMessageEvent(@PathVariable Long gameId, @PathVariable Long messageId, @PathVariable String type){
        this.notificationService.sendNotification(gameId, messageId, type);
    }

    @GetMapping("subscribeMessage/{gameId}")
    public SseEmitter subscribeMessage(@PathVariable Long gameId){
        return this.emitterService.createEmitter(gameId + "message");
    }

    @GetMapping("/subscribeLineup/{gameId}")
    public SseEmitter subscribeLineup(@PathVariable Long gameId){
        return this.emitterService.createEmitter(gameId + "lineup");
    }

    @GetMapping("/subscribeReporter/{gameId}")
    public SseEmitter subscribeReporter(@PathVariable Long gameId){
        return this.emitterService.createEmitter(gameId + "reporter");
    }

    @GetMapping("/subscribeStatistics/{gameId}")
    public SseEmitter subscribeToStatistics(@PathVariable Long gameId){
        return this.emitterService.createEmitter(gameId + "stats");
    }

    @GetMapping("/subscribe/{gameId}")
    public SseEmitter subscribeToEvents(@PathVariable Long gameId) {
        return this.emitterService.createEmitter(Long.toString(gameId));
    }
}
