package com.example.demo.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class AddReportRequest {
    private String reportStatus;
    private String reportType;
    private String extraMessage;
    private Long gameId;
    private String userMail;
}
