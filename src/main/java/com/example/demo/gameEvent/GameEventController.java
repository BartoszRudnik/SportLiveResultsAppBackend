package com.example.demo.gameEvent;

import com.example.demo.gameEvent.dto.FullEventRequest;
import com.example.demo.gameEvent.dto.IncompleteEventRequest;
import com.example.demo.gameEvent.dto.UpdateEventRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/gameEvent")
@AllArgsConstructor
public class GameEventController {
    private final GameEventService gameEventService;

    @PostMapping("/substitution/{playerOffId}/{playerOnId}/{gameId}/{message}")
    public List<Long> substitution(@PathVariable Long playerOffId, @PathVariable Long playerOnId, @PathVariable Long gameId, @PathVariable String message){
        return this.gameEventService.substitution(playerOffId, playerOnId, gameId, message);
    }

    @PostMapping("/gameStart/{gameId}")
    public void gameStart(@PathVariable Long gameId){
        this.gameEventService.gameStart(gameId);
    }

    @PostMapping("/halfStart/{gameId}")
    public void halfStart(@PathVariable Long gameId){
        this.gameEventService.halfStart(gameId);
    }

    @PostMapping("/halfEnd/{gameId}")
    public void halfEnd(@PathVariable Long gameId){
        this.gameEventService.halfEnd(gameId);
    }

    @PostMapping("/gameEnd/{gameId}")
    public void gameEnd(@PathVariable Long gameId){
        this.gameEventService.gameEnd(gameId);
    }

    @GetMapping("/getGameEvents/{gameId}")
    public List<GameEvent> getGameEvents(@PathVariable Long gameId){
        return this.gameEventService.findAllByGame(gameId);
    }

    @PostMapping("/addIncompleteGameEvent")
    public Long addIncompleteGameEvent(@RequestBody IncompleteEventRequest request){
       return this.gameEventService.addIncompleteGameEvent(request);
    }

    @PostMapping("/addFullGameEvent")
    public Long addFullGameEvent(@RequestBody FullEventRequest request){
        return this.gameEventService.addFullGameEvent(request);
    }

    @PostMapping("/updateGameEvent")
    public void updateGameEvent(@RequestBody UpdateEventRequest request){
        this.gameEventService.updateEvent(request);
    }

    @DeleteMapping("/deleteGameEvent/{id}")
    public void deleteGameEvent(@PathVariable Long id){
        this.gameEventService.deleteGameEvent(id);
    }
}
