package com.example.demo.report;

import com.example.demo.report.dto.AddReportRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public void addReport(AddReportRequest addReportRequest) {
    }
}
