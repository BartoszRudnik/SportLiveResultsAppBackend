package com.example.demo.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Repository
public class InMemoryEmitterRepository implements EmitterRepository{
    private final Map<String, List<SseEmitter>> userEmitterMap = new ConcurrentHashMap<>();

    @Override
    public void addOrReplaceEmitter(String memberId, SseEmitter emitter) {
        if(!this.userEmitterMap.containsKey(memberId)){
            this.userEmitterMap.put(memberId, new ArrayList<>());
        }
        this.userEmitterMap.get(memberId).add(emitter);
    }

    @Override
    public void remove(String memberId, SseEmitter emitter) {
        this.userEmitterMap.get(memberId).remove(emitter);
    }

    @Override
    public Optional<List<SseEmitter>> get(String memberId) {
        return Optional.ofNullable(this.userEmitterMap.get(memberId));
    }
}
