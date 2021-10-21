package com.example.demo.player.dto;

import com.example.demo.league.dto.GameEventsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetPlayerGamesResponse {

    private Long gameId;
    private Long teamAId;
    private Long teamBId;
    private int scoreTeamA;
    private int scoreTeamB;
    private LocalDateTime gameDate;
    private String stadiumName;
    private List<GameEventsResponse> events;
    private List<Long> squadTeamA;
    private List<Long> squadTeamB;
    private List<Long> substitutionsTeamA;
    private List<Long> substitutionsTeamB;
    private int round;
    private Long numberOfMinutes;
    private int numberOfGoals;
    private boolean isYellowCard;
    private boolean isRedCard;

}
