package com.example.demo.league.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetLeagueTableResponse implements Comparable<GetLeagueTableResponse>{
    private String teamName;
    private int games;
    private int goalScored;
    private int goalLost;
    private int points;

    @Override
    public int compareTo(GetLeagueTableResponse u) {
        return Integer.compare(this.points, u.points);
    }
}
