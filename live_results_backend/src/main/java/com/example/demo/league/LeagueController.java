package com.example.demo.league;

import com.example.demo.game.Game;
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

    @GetMapping("/getGamesByRound/{leagueId}/{round}")
    public List<Game> getGamesByRound(@PathVariable Long leagueId, @PathVariable int round){
        return this.leagueService.getGamesByRound(leagueId, round);
    }

}
