package com.example.demo.appUser.dto;

import com.example.demo.league.dto.GameEventsResponse;
import com.example.demo.player.dto.SinglePlayerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetFavoriteGamesResponse {
    private Long gameId;
    private Long leagueId;
    private Long teamAId;
    private Long teamBId;
    private int scoreTeamA;
    private int scoreTeamB;
    private LocalDateTime gameDate;
    private LocalDateTime actualGameStart;
    private String stadiumName;
    private List<GameEventsResponse> events;
    private List<SinglePlayerResponse> squadTeamA;
    private List<SinglePlayerResponse> squadTeamB;
    private List<SinglePlayerResponse> substitutionsTeamA;
    private List<SinglePlayerResponse> substitutionsTeamB;
    private int round;
    private boolean halfBreak;
    private int partOfGame;
    private int lengthOfPartOfGame;
    private String reporterMail;
    private boolean isNotification;
    private String gameStatus;
}
