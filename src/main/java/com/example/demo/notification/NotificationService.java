package com.example.demo.notification;

import com.example.demo.notification.dto.DeleteGameEventRequest;
import com.example.demo.notification.dto.NewEventDto;
import com.example.demo.notification.dto.NewTimeEventDto;
import com.example.demo.notification.dto.UpdateGameEventRequest;

public interface NotificationService {
    void sendNotification(Long gameId, String subtitle);
    void sendNotification(NewEventDto request);
    void sendNotification(NewTimeEventDto request);
    void sendNotification(Long gameId);
    void sendNotification(DeleteGameEventRequest request);
    void sendNotification(UpdateGameEventRequest request);
    void sendNotification(Long gameId, Long leagueId);
    void sendNotification(Long gameId, Long messageId, String message);
    void sendNotification(String userMail, Long reportId);
}
