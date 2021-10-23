package com.example.demo.notification;

import com.example.demo.notification.dto.NewEventDto;
import com.example.demo.notification.dto.NewTimeEventDto;

public interface NotificationService {
    void sendNotification(NewEventDto request);
    void sendNotification(NewTimeEventDto request);
}
