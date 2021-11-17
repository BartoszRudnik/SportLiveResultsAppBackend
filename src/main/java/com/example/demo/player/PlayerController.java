package com.example.demo.player;

import com.example.demo.player.dto.AddPlayerRequest;
import com.example.demo.player.dto.GetPlayerGamesResponse;
import com.example.demo.player.dto.SinglePlayerResponse;
import com.example.demo.player.dto.UpdatePlayerDataRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/player")
@AllArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping("/getSinglePlayer/{playerId}")
    public SinglePlayerResponse getSinglePlayer(@PathVariable Long playerId){
        return this.playerService.getSinglePlayer(playerId);
    }

    @GetMapping("/getPlayerFinishedAndLiveGames/{playerId}")
    public List<GetPlayerGamesResponse> getPlayerFinishedAndLiveGames(@PathVariable Long playerId){
        return this.playerService.getPlayerFinishedAndLiveGames(playerId);
    }

    @GetMapping("/getPlayersFromLeague/{leagueId}")
    public List<SinglePlayerResponse> getPlayersFromLeague(@PathVariable Long leagueId){
        return this.playerService.getPlayerFromLeague(leagueId);
    }

    @PostMapping("/addPlayer")
    public void addPlayer(@RequestBody AddPlayerRequest request){
        this.playerService.addPlayer(request);
    }

    @GetMapping("/getPlayer/{id}")
    public Player getPlayer(@PathVariable Long id){
        return this.playerService.getPlayer(id);
    }

    @PostMapping("/updatePlayerTeam/{playerId}/{newTeamId}")
    public void updatePlayerTeam(@PathVariable Long playerId, @PathVariable Long newTeamId){
        this.playerService.updatePlayerTeam(playerId, newTeamId);
    }

    @PostMapping("/updatePlayerData/{playerId}")
    public void updatePlayerData(@PathVariable Long playerId, @RequestBody UpdatePlayerDataRequest request){
        this.playerService.updatePlayerData(playerId, request);
    }
}
