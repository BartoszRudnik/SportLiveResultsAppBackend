package com.example.demo.report;

import com.example.demo.appUser.AppUser;
import com.example.demo.appUser.AppUserService;
import com.example.demo.game.Game;
import com.example.demo.game.GameService;
import com.example.demo.report.dto.AddReportRequest;
import com.example.demo.report.dto.GetReportRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final AppUserService appUserService;
    private final GameService gameService;

    public void addReport(AddReportRequest addReportRequest) {
        if(this.appUserService.findByEmail(addReportRequest.getUserMail()).isPresent() && this.gameService.getGameOptional(addReportRequest.getGameId()).isPresent()){
            Report report = new Report();

            report.setReportType(this.stringToReportType(addReportRequest.getReportType()));
            report.setReportStatus(this.stringToReportStatus(addReportRequest.getReportStatus()));
            report.setAppUser(this.appUserService.findByEmail(addReportRequest.getUserMail()).get());
            report.setGame(this.gameService.getGameOptional(addReportRequest.getGameId()).get());
            report.setExtraMessage(addReportRequest.getExtraMessage());

            this.reportRepository.save(report);
        }
    }

    public List<GetReportRequest> getAllUserReports(String userMail) {
        if(this.appUserService.findByEmail(userMail).isPresent()){
            AppUser user = this.appUserService.findByEmail(userMail).get();

            List<Report> userReports = this.reportRepository.findAllByAppUser(user);
            List<GetReportRequest> result = new ArrayList<>();

            for(Report report : userReports){
                result.add(new GetReportRequest(report.getId(), report.getGame().getId(), report.getAppUser().getEmail(), report.getReportStatus().toString(), report.getReportType().toString(), report.getExtraMessage()));
            }

            return result;
        }else{
            return new ArrayList<>();
        }
    }

    public List<GetReportRequest> getAllGameReports(Long gameId) {
        if(this.gameService.getGameOptional(gameId).isPresent()){
            Game game = this.gameService.getGameOptional(gameId).get();

            List<Report> gameReports = this.reportRepository.findAllByGame(game);
            List<GetReportRequest> result = new ArrayList<>();

            for(Report report : gameReports){
                result.add(new GetReportRequest(report.getId(), report.getGame().getId(), report.getAppUser().getEmail(), report.getReportStatus().toString(), report.getReportType().toString(), report.getExtraMessage()));
            }

            return result;
        }else{
            return new ArrayList<>();
        }
    }

    public GetReportRequest getSingleReport(Long reportId) {
        if(this.reportRepository.findById(reportId).isPresent()){
            Report report = this.reportRepository.findById(reportId).get();

            return new GetReportRequest(report.getId(), report.getGame().getId(), report.getAppUser().getEmail(), report.getReportStatus().toString(), report.getReportType().toString(), report.getExtraMessage());
        }else{
            return new GetReportRequest();
        }
    }

    public void changeReportStatus(Long reportId, String newStatus) {
        if(this.reportRepository.findById(reportId).isPresent()){
            Report report = this.reportRepository.findById(reportId).get();

            report.setReportStatus(this.stringToReportStatus(newStatus));

            this.reportRepository.save(report);
        }
    }

    public void deleteReport(Long reportId) {
        if(this.reportRepository.findById(reportId).isPresent()){
           Report report = this.reportRepository.findById(reportId).get();

           if(report.getReportStatus().equals(ReportStatus.WAITING)){
               this.reportRepository.delete(report);
           }
        }
    }

    private ReportType stringToReportType(String reportType){
        if(reportType.equalsIgnoreCase("result")){
            return ReportType.RESULT;
        }else if(reportType.equalsIgnoreCase("goal")){
            return ReportType.GOAL;
        }else if(reportType.equalsIgnoreCase("card")){
            return ReportType.CARD;
        }else if(reportType.equalsIgnoreCase("squad")){
            return ReportType.SQUAD;
        }else {
            return ReportType.OTHER;
        }
    }

    private ReportStatus stringToReportStatus(String gameStatus){
        if(gameStatus.equalsIgnoreCase("waiting")){
            return ReportStatus.WAITING;
        }else if(gameStatus.equalsIgnoreCase("In_progress")){
            return ReportStatus.IN_PROGRESS;
        }else if(gameStatus.equalsIgnoreCase("accepted")){
            return ReportStatus.ACCEPTED;
        }else{
            return ReportStatus.DECLINED;
        }
    }
}
