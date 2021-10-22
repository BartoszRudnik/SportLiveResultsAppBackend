package com.example.demo.notification;

import com.example.demo.notification.dto.NewEventDto;

public interface NotificationService {
    void sendNotification(NewEventDto request);
}
