package com.example.demo.game.dto;

import com.example.demo.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetLineupsResponse {
    private Set<Long> squadTeamA;
    private Set<Long> squadTeamB;
    private Set<Long> substitutionsTeamA;
    private Set<Long> substitutionsTeamB;
}
