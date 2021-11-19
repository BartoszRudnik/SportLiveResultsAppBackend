package com.example.demo.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddMessageRequest {
    private String text;
    private LocalDateTime date;
    private String userMail;
    private Long gameId;
}
