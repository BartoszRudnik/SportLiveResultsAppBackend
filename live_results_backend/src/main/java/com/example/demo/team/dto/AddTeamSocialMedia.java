package com.example.demo.team.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddTeamSocialMedia {
    private Long teamId;
    private String facebookUrl;
    private String twitterUrl;
    private String instagramUrl;
}
