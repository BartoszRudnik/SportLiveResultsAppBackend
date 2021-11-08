package com.example.demo.gameStatistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class GetGameStatistics {
    private int foulsTeamA;
    private int foulsTeamB;
    private int cornersTeamA;
    private int cornersTeamB;
    private int offsidesTeamA;
    private int offsidesTeamB;
    private int shootsTeamA;
    private int shootsTeamB;
}
