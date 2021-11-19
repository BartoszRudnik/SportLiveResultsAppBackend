package com.example.demo.league.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetGamesResponse {
    private Long gameId;
    private Long teamAId;
    private Long teamBId;
    private int scoreTeamA;
    private int scoreTeamB;
    private LocalDateTime gameDate;
    private LocalDateTime actualGameStart;
    private String stadiumName;
    private List<GameEventsResponse> events;
    private List<Long> squadTeamA;
    private List<Long> squadTeamB;
    private List<Long> substitutionsTeamA;
    private List<Long> substitutionsTeamB;
    private int round;
    private boolean halfBreak;
    private int partOfGame;
    private int lengthOfPartOfGame;
    private String reporterMail;
}
