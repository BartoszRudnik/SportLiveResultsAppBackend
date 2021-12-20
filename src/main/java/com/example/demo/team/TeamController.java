package com.example.demo.team;

import com.example.demo.game.Game;
import com.example.demo.league.dto.GetGamesResponse;
import com.example.demo.player.Player;
import com.example.demo.team.dto.AddTeamRequest;
import com.example.demo.team.dto.AddTeamSocialMedia;
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

    @GetMapping("/getSingleTeam/{teamId}/{email}")
    public SingleTeamResponse getSingleTeam(@PathVariable Long teamId, @PathVariable String email){
        return this.teamService.getSingleTeam(teamId, email);
    }

    @PostMapping("/addTeamSocialMedia")
    public void addTeamSocialMedia(@RequestBody AddTeamSocialMedia request){
        this.teamService.addSocialMedia(request);
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
    public List<GetGamesResponse> getAllTeamFinishedGames(@PathVariable Long teamId){
        return this.teamService.getAllTeamFinishedGames(teamId);
    }

    @GetMapping("/getAllTeamScheduledGames/{teamId}")
    public List<GetGamesResponse> getAllTeamScheduledGames(@PathVariable Long teamId){
        return this.teamService.getAllTeamScheduledGame(teamId);
    }
}
