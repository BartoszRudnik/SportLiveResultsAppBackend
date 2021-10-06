package com.example.demo.team.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddTeamRequest {

    private String teamName;
    private String city;
    private String stadiumName;
    private Long leagueId;
    private List<Long> players;

}
