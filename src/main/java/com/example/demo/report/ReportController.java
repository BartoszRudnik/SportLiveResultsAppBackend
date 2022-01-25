package com.example.demo.report;


import com.example.demo.report.dto.AddReportRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/report")
@AllArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/addReport")
    public void addReport(@RequestBody AddReportRequest addReportRequest){
        this.reportService.addReport(addReportRequest);
    }

}
