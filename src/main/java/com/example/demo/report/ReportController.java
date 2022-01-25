package com.example.demo.report;


import com.example.demo.report.dto.AddReportRequest;
import com.example.demo.report.dto.GetReportRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getSingleReport/{reportId}")
    public GetReportRequest getSingleReport(@PathVariable Long reportId){
        return this.reportService.getSingleReport(reportId);
    }

    @GetMapping("/getAllUserReports/{userMail}")
    public List<GetReportRequest> getAllUserReports(@PathVariable String userMail){
        return this.reportService.getAllUserReports(userMail);
    }

    @GetMapping("/getAllGameReports/{gameId}")
    public List<GetReportRequest> getAllGameReports(@PathVariable Long gameId){
        return this.reportService.getAllGameReports(gameId);
    }

    @DeleteMapping("/deleteReport/{reportId}")
    public void deleteReport(@PathVariable Long reportId){
        this.reportService.deleteReport(reportId);
    }

    @PutMapping("/changeReportStatus/{reportId}/{newStatus}")
    public void changeReportStatus(@PathVariable Long reportId, @PathVariable String newStatus){
        this.reportService.changeReportStatus(reportId, newStatus);
    }
}
