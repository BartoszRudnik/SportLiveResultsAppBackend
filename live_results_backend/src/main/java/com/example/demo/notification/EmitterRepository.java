package com.example.demo.notification;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

public interface EmitterRepository {
    void addOrReplaceEmitter(String memberId, SseEmitter emitter);
    void remove(String memberId, SseEmitter emitter);
    Optional<List<SseEmitter>> get(String memberId);
}
