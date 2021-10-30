package com.example.demo.notification;

import com.example.demo.notification.dto.NewEventDto;
import com.example.demo.notification.dto.NewTimeEventDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping(path = "api/v1/notification")
@AllArgsConstructor
public class SseController {
    private final EmitterService  emitterService;
    private final NotificationService notificationService;

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

    @GetMapping("/subscribeStatistics/{gameId}")
    public SseEmitter subscribeToStatistics(@PathVariable Long gameId){
        return this.emitterService.createEmitter(gameId + "stats");
    }

    @GetMapping("/subscribe/{gameId}")
    public SseEmitter subscribeToEvents(@PathVariable Long gameId) {
        return this.emitterService.createEmitter(Long.toString(gameId));
    }
}
