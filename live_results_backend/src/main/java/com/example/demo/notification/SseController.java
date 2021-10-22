package com.example.demo.notification;

import com.example.demo.notification.dto.NewEventDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping(path = "api/v1/notification")
@AllArgsConstructor
public class SseController {
    private final EmitterService  emitterService;
    private final NotificationService notificationService;

    @PostMapping("/publishNewEvent")
    public void publishNewEvent(@RequestBody NewEventDto request){
        this.notificationService.sendNotification(request);
    }

    @GetMapping("/subscribe/{gameId}")
    public SseEmitter subscribeToEvents(@PathVariable int gameId) {
        return this.emitterService.createEmitter(Integer.toString(gameId));
    }
}
