package com.example.demo.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class MessageResponse {
    private String text;
    private LocalDateTime date;
    private String userMail;
    private Long gameId;
    private Long messageId;
    private boolean deleted;
    private Long parentMessageId;
    private String eventName;

    public MessageResponse(String text, LocalDateTime date, String userMail, Long gameId, Long messageId, boolean deleted, String eventName){
         this.text = text;
         this.date = date;
         this.userMail = userMail;
         this.gameId = gameId;
         this.messageId = messageId;
         this.deleted = deleted;
         this.eventName = eventName;
    }
}
