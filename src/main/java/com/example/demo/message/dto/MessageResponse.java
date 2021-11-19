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
}
