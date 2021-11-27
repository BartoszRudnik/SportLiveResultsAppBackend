package com.example.demo.gameStatistics;


import com.example.demo.gameStatistics.dto.GetGameStatistics;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/gameStatistics")
@AllArgsConstructor
public class GameStatisticsController {
    private final GameStatisticsService gameStatisticsService;

    @GetMapping("/getStatistics/{gameId}")
    public GetGameStatistics getGameStatistics(@PathVariable Long gameId){
        return this.gameStatisticsService.getGameStatistics(gameId);
    }

    @PostMapping("/addShoot/{gameId}/{teamId}")
    public Long addShoot(@PathVariable Long gameId, @PathVariable Long teamId){
        return this.gameStatisticsService.addShoot(teamId, gameId);
    }

    @PostMapping("/addCorner/{gameId}/{teamId}")
    public Long addCorner(@PathVariable Long gameId, @PathVariable Long teamId){
        return this.gameStatisticsService.addCorner(teamId, gameId);
    }

    @PostMapping("/addFoul/{gameId}/{teamId}")
    public Long addFoul(@PathVariable Long gameId, @PathVariable Long teamId){
        return this.gameStatisticsService.addFoul(teamId, gameId);
    }

    @PostMapping("/addOffside/{gameId}/{teamId}")
    public Long addOffside(@PathVariable Long gameId, @PathVariable Long teamId){
        return this.gameStatisticsService.addOffside(teamId, gameId);
    }
}
