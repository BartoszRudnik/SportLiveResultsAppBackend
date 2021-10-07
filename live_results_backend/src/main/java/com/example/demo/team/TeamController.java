package com.example.demo.team;

import com.example.demo.game.Game;
import com.example.demo.player.Player;
import com.example.demo.team.dto.AddTeamRequest;
import com.example.demo.team.dto.SingleTeamResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/team")
@AllArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/getTeamsFromLeague/{leagueId}")
    public List<SingleTeamResponse> getTeamsFromLeague(@PathVariable Long leagueId){
        return this.teamService.getTeamsFromLeague(leagueId);
    }

    @PostMapping("/addTeam")
    public Long addTeam(@RequestBody AddTeamRequest request){
        return this.teamService.addTeam(request);
    }

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
