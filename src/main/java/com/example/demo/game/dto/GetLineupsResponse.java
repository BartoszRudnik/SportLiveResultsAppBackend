package com.example.demo.game.dto;

import com.example.demo.player.Player;
import com.example.demo.player.dto.GamePlayerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetLineupsResponse {
    private Set<GamePlayerResponse> squadTeamA;
    private Set<GamePlayerResponse> squadTeamB;
    private Set<GamePlayerResponse> substitutionsTeamA;
    private Set<GamePlayerResponse> substitutionsTeamB;
}
