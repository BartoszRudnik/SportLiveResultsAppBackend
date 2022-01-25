package com.example.demo.report;

import com.example.demo.appUser.AppUser;
import com.example.demo.game.Game;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "Report")
public class Report {

    @Id
    @SequenceGenerator(
            name = "report_sequence",
            sequenceName = "report_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "report_sequence"
    )
    private Long id;

    private ReportStatus reportStatus;
    private ReportType reportType;
    private String extraMessage;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    public Report(ReportStatus reportStatus, ReportType reportType, String extraMessage, Game game, AppUser appUser){
        this.reportStatus = reportStatus;
        this.reportType = reportType;
        this.extraMessage = extraMessage;
        this.game = game;
        this.appUser = appUser;
    }
}
