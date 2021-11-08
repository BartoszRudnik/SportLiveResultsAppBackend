package com.example.demo.team.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SingleTeamResponse {
    private Long teamId;
    private String teamName;
    private String city;
    private String stadiumName;
    private String facebookUrl;
    private String twitterUrl;
    private String instagramUrl;
}
