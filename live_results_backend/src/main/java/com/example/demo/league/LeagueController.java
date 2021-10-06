package com.example.demo.league;

import com.example.demo.game.Game;
import com.example.demo.league.dto.AddLeagueRequest;
import com.example.demo.leagueTable.LeagueTable;
import com.example.demo.player.Player;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/league")
@AllArgsConstructor
public class LeagueController {

    private final LeagueService leagueService;

    @PostMapping("/addLeague")
    public Long addLeague(@RequestBody AddLeagueRequest request){
        return this.leagueService.addLeague(request);
    }

    @GetMapping("/getBestScorers/{leagueId}")
    public List<Player> getBestScorers(@PathVariable Long leagueId){
        return this.leagueService.getLeagueBestScorers(leagueId);
    }

    @GetMapping("/getBestAssistants/{leagueId}")
    public List<Player> getBestAssistants(@PathVariable Long leagueId){
        return this.leagueService.getLeagueBestAssistants(leagueId);
    }

    @GetMapping("/getBestCanadianPoints/{leagueId}")
    public List<Player> getBestCanadianPoints(@PathVariable Long leagueId){
        return this.leagueService.getLeagueBestCanadianPoints(leagueId);
    }

    @GetMapping("/getLeagueTable/{leagueId}")
    public List<LeagueTable> getLeagueTable(@PathVariable Long leagueId){
        return this.leagueService.getLeagueTable(leagueId);
    }

    @GetMapping("/getAllGamesByRound/{leagueId}/{round}")
    public List<Game> getAllGamesByRound(@PathVariable Long leagueId, @PathVariable int round){
        return this.leagueService.getGamesByRound(leagueId, round);
    }

    @GetMapping("/getLiveGamesByRound/{leagueId}/{round}")
    public List<Game> getLiveGamesByRound(@PathVariable Long leagueId, @PathVariable int round){
        return this.leagueService.getLiveGamesByRound(leagueId, round);
    }

    @GetMapping("/getScheduledGamesByRound/{leagueId}/{round}")
    public List<Game> getScheduledGamesByRound(@PathVariable Long leagueId, @PathVariable int round){
        return this.leagueService.getScheduledGamesByRound(leagueId, round);
    }

    @GetMapping("/getFinishedGamesByRound/{leagueId}/{round}")
    public List<Game> getFinishedGamesByRound(@PathVariable Long leagueId, @PathVariable int round){
        return this.leagueService.getFinishedGamesByRound(leagueId, round);
    }
}
