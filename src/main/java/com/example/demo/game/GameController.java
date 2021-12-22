package com.example.demo.game;

import com.example.demo.game.dto.*;
import com.example.demo.gameEvent.GameEvent;
import com.example.demo.league.dto.GameEventsResponse;
import com.example.demo.league.dto.GetGamesResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/game")
@AllArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping("/getGameEvents/{gameEventId}")
    public GameEventsResponse getGameEvent(@PathVariable Long gameEventId){
        return this.gameService.getGameEventsResponse(gameEventId);
    }

    @GetMapping("/getGame/{gameId}")
    public Game getGame(@PathVariable Long gameId){
        return this.gameService.getGame(gameId);
    }

    @PostMapping("/addGame")
    public Long addGame(@RequestBody AddGameRequest request){
        return this.gameService.addGame(request);
    }

    @PostMapping("/updateScore/{gameId}")
    public void updateScore(@PathVariable Long gameId, @RequestBody UpdateScoreRequest request){
        this.gameService.updateScore(gameId, request);
    }

    @GetMapping("/getSingleGame/{gameId}")
    public GetGamesResponse getSingleGame(@PathVariable Long gameId){
        return this.gameService.getSingleGame(gameId);
    }

    @PostMapping("/addLineups/{gameId}")
    public void addLineups(@PathVariable Long gameId, @RequestBody AddLineupsRequest request){
        this.gameService.addLineups(gameId, request);
    }

    @GetMapping("/getLineups/{gameId}")
    public GetLineupsResponse getLineups(@PathVariable Long gameId){
        return this.gameService.getLineups(gameId);
    }

    @PostMapping("/changeGameStatus/{gameId}/{newStatus}")
    public void changeGameStatus(@PathVariable Long gameId, @PathVariable String newStatus){
        this.gameService.changeGameStatus(gameId, newStatus);
    }

    @GetMapping("/getReporter/{gameId}")
    public String getReporter(@PathVariable Long gameId){
        return this.gameService.getReporter(gameId);
    }

    @PostMapping("/addReporter/{userMail}/{gameId}")
    public void addReporter(@PathVariable String userMail, @PathVariable Long gameId){
        this.gameService.addReporter(userMail, gameId);
    }

    @PostMapping("/removeReporter/{gameId}")
    public void removeReporter(@PathVariable Long gameId){
        this.gameService.removeReporter(gameId);
    }

    @GetMapping("/isReporter/{gameId}")
    public boolean isReporter(@PathVariable Long gameId){
        return this.gameService.isReporter(gameId);
    }

    @GetMapping("/getSquadPlayerInfo/{teamId}")
    public List<SquadPlayerInfo> getSquadPlayerInfo(@PathVariable Long teamId){
        return this.gameService.getSquadPlayerInfo(teamId);
    }
}
