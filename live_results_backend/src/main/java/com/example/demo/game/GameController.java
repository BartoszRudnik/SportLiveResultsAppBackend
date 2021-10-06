package com.example.demo.game;

import com.example.demo.game.dto.AddGameRequest;
import com.example.demo.game.dto.AddLineupsRequest;
import com.example.demo.game.dto.GetLineupsResponse;
import com.example.demo.game.dto.UpdateScoreRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/game")
@AllArgsConstructor
public class GameController {
    private final GameService gameService;

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
}
