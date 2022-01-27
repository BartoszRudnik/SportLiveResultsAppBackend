package com.example.demo.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class GetReportRequest {
    private Long reportId;
    private Long gameId;
    private Long leagueId;
    private String userMail;
    private String reportStatus;
    private String reportType;
    private String extraMessage;
    private LocalDateTime timeOfSending;
}
