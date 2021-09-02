package com.example.demo.gameEvent;

import com.example.demo.gameEvent.dto.FullEventRequest;
import com.example.demo.gameEvent.dto.IncompleteEventRequest;
import com.example.demo.gameEvent.dto.UpdateEventRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/gameEvent")
@AllArgsConstructor
public class GameEventController {
    private final GameEventService gameEventService;

    @GetMapping("/getGameEvents/{gameId}")
    public List<GameEvent> getGameEvents(@PathVariable Long gameId){
        return this.gameEventService.findAllByGame(gameId);
    }

    @PostMapping("/addIncompleteGameEvent")
    public void addIncompleteGameEvent(@RequestBody IncompleteEventRequest request){
        this.gameEventService.addIncompleteGameEvent(request);
    }

    @PostMapping("/addFullGameEvent")
    public void addFullGameEvent(@RequestBody FullEventRequest request){
        this.gameEventService.addFullGameEvent(request);
    }

    @PostMapping("/updateGameEvent")
    public void updateGameEvent(@RequestBody UpdateEventRequest request){
        this.gameEventService.updateEvent(request);
    }

    @DeleteMapping("/deleteGameEvent/{id}")
    public void deleteGameEvent(@PathVariable Long id){
        this.gameEventService.deleteById(id);
    }
}
