package com.example.demo.team;

import com.example.demo.game.Game;
import com.example.demo.player.Player;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/team")
@AllArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/getTeam/{teamId}")
    public Team getTeam(@PathVariable Long teamId){
        return this.teamService.getTeam(teamId);
    }

    @GetMapping("/getTeamPlayers/{teamId}")
    public List<Player> getTeamPlayers(@PathVariable Long teamId){
        return this.teamService.getTeamPlayers(teamId);
    }

    @GetMapping("/getAllTeamGames/{teamId}")
    public List<Game> getAllTeamGames(@PathVariable Long teamId){
        return this.teamService.getAllTeamGames(teamId);
    }

    @GetMapping("/getAllTeamFinishedGames/{teamId}")
    public List<Game> getAllTeamFinishedGames(@PathVariable Long teamId){
        return this.teamService.getAllTeamFinishedGames(teamId);
    }

    @GetMapping("/getAllTeamScheduledGames/{teamId}")
    public List<Game> getAllTeamScheduledGames(@PathVariable Long teamId){
        return this.teamService.getAllTeamScheduledGame(teamId);
    }
}
