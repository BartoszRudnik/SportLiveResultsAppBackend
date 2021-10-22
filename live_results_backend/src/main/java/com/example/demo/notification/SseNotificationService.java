package com.example.demo.notification;

import com.example.demo.appUser.AppUserRepository;
import com.example.demo.notification.dto.NewEventDto;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@Primary
@AllArgsConstructor
public class SseNotificationService implements NotificationService{
    private final EmitterRepository emitterRepository;
    private final EventMapper eventMapper;

    @Override
    public void sendNotification(NewEventDto request) {
        if(request != null){
            this.doSendNotification(request);
        }
    }

    private void doSendNotification(NewEventDto request) {
        if(this.emitterRepository.get(Integer.toString(request.getGameId())).isPresent()) {
            List<SseEmitter> emitters = this.emitterRepository.get(Integer.toString(request.getGameId())).get();

            for (SseEmitter emitter : emitters) {
                if(emitter != null) {
                    try {
                        emitter.send(this.eventMapper.toSseEventBuilder(request));
                    } catch (IOException | IllegalStateException e) {
                        this.emitterRepository.remove(Integer.toString(request.getGameId()), emitter);
                    }
                }
            }
        }
    }
}
