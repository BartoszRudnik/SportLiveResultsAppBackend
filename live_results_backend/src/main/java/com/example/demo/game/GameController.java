package com.example.demo.game;

import com.example.demo.game.dto.AddGameRequest;
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

    @PostMapping("/changeGameStatus/{gameId}/{newStatus}")
    public void changeGameStatus(@PathVariable Long gameId, @PathVariable String newStatus){
        this.gameService.changeGameStatus(gameId, newStatus);
    }
}
