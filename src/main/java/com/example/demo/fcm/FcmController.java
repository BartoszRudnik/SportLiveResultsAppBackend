package com.example.demo.fcm;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/fcm")
@AllArgsConstructor
public class FcmController {
    private final FcmService fcmService;

    @PostMapping("/message/{gameId}")
    public String sendGoalFcm(@PathVariable Long gameId, @RequestBody Note note) throws FirebaseMessagingException {
      return this.fcmService.sendNotification(note, gameId);
    }
}
