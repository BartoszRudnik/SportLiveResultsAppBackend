package com.example.demo.player;

import com.example.demo.player.dto.AddPlayerRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/player")
@AllArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/addPlayer")
    public void addPlayer(@RequestBody AddPlayerRequest request){
        this.playerService.addPlayer(request);
    }

    @GetMapping("/getPlayer/{id}")
    public Player getPlayer(@PathVariable Long id){
        return this.playerService.getPlayer(id);
    }

}
