package com.example.demo.league.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetLeaguesResponse {
    private Long leagueId;
    private String leagueName;
    private String leagueLevel;
}
