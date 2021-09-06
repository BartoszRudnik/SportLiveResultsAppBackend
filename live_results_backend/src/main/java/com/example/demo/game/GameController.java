package com.example.demo.game;

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

}
