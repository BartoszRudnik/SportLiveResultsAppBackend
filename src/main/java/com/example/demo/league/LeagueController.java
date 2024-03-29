package com.example.demo.league;

import com.example.demo.game.Game;
import com.example.demo.league.dto.AddLeagueRequest;
import com.example.demo.league.dto.GetGamesResponse;
import com.example.demo.league.dto.GetLeagueTableResponse;
import com.example.demo.league.dto.GetLeaguesResponse;
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

    @GetMapping("/getLeagues/{leagueLevel}")
    public List<GetLeaguesResponse> getLeagues(@PathVariable String leagueLevel){
        return this.leagueService.findAllByLeagueLevel(leagueLevel);
    }

    @GetMapping("/getLeague/{leagueId}")
    public GetLeaguesResponse getSingleLeague(@PathVariable Long leagueId){
        return this.leagueService.findSingleLeague(leagueId);
    }

    @PostMapping("/addLeague")
    public Long addLeague(@RequestBody AddLeagueRequest request){
        return this.leagueService.addLeague(request);
    }

    @GetMapping("/getLeagueTable/{leagueId}")
    public List<GetLeagueTableResponse> getLeagueTable(@PathVariable Long leagueId){
        return this.leagueService.getLeagueTable(leagueId);
    }

    @GetMapping("/getFormTable/{leagueId}/{size}")
        public List<GetLeagueTableResponse> getFormTable5(@PathVariable Long leagueId, @PathVariable int size){
        return this.leagueService.getFormTable(leagueId, size);
    }

    @GetMapping("/getHomeTable/{leagueId}")
    public List<GetLeagueTableResponse> getHomeTable(@PathVariable Long leagueId){
        return this.leagueService.getHomeTable(leagueId);
    }

    @GetMapping("/getAwayTable/{leagueId}")
    public List<GetLeagueTableResponse> getAwayTable(@PathVariable Long leagueId){
        return this.leagueService.getAwayTable(leagueId);
    }

    @GetMapping("/getAllGamesByRound/{leagueId}/{round}")
    public List<GetGamesResponse> getAllGamesByRound(@PathVariable Long leagueId, @PathVariable int round){
        return this.leagueService.getGamesByRound(leagueId, round);
    }

    @GetMapping("/getAllFinishedByLeague/{leagueId}")
    public List<GetGamesResponse> getAllFinishedByLeague(@PathVariable Long leagueId){
        return this.leagueService.getAllFinishedByLeague(leagueId);
    }

    @GetMapping("/getLiveGamesByRound/{leagueId}/{round}")
    public List<Long> getLiveGamesByRound(@PathVariable Long leagueId, @PathVariable int round){
        return this.leagueService.getLiveGamesByRound(leagueId, round);
    }

    @GetMapping("/getScheduledGamesByRound/{leagueId}/{round}")
    public List<Long> getScheduledGamesByRound(@PathVariable Long leagueId, @PathVariable int round){
        return this.leagueService.getScheduledGamesByRound(leagueId, round);
    }

    @GetMapping("/getFinishedGamesByRound/{leagueId}/{round}")
    public List<Long> getFinishedGamesByRound(@PathVariable Long leagueId, @PathVariable int round){
        return this.leagueService.getFinishedGamesByRound(leagueId, round);
    }
}
