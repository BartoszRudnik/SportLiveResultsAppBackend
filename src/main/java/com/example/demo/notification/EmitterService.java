package com.example.demo.notification;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class EmitterService {
    private final long eventsTimeout;
    private final EmitterRepository repository;

    public EmitterService(EmitterRepository repository){
        this.eventsTimeout = -1L;
        this.repository = repository;
    }

    public SseEmitter createEmitter(String memberId){
        SseEmitter emitter = new SseEmitter(this.eventsTimeout);

//        emitter.onCompletion(() -> this.repository.remove(memberId, emitter));
//        emitter.onTimeout(() -> this.repository.remove(memberId, emitter));
//        emitter.onError(e -> this.repository.remove(memberId, emitter));

        this.repository.addOrReplaceEmitter(memberId, emitter);

        return emitter;
    }

}
