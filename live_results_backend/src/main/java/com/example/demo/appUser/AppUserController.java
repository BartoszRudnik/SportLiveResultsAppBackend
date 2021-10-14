package com.example.demo.appUser;

import com.example.demo.appUser.dto.UserFavoritesRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/user")
@AllArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping("/changeTeamFavoriteStatus/{userMail}/{teamId}")
    public void changeTeamFavoriteStatus(@PathVariable String userMail, @PathVariable Long teamId){
        this.appUserService.changeTeamFavoriteStatus(userMail, teamId);
    }

    @PostMapping("/changeGameFavoriteStatus/{userMail}/{gameId}")
    public void changeGameFavoriteStatus(@PathVariable String userMail, @PathVariable Long gameId){
        this.appUserService.changeGameFavoriteStatus(userMail, gameId);
    }

    @PostMapping("/changeLeagueFavoriteStatus/{userMail}/{leagueId}")
    public void changeLeagueFavoriteStatus(@PathVariable String userMail, @PathVariable Long leagueId){
        this.appUserService.changeLeagueFavoriteStatus(userMail, leagueId);
    }

    @GetMapping("/getUserFavorites/{userMail}")
    public UserFavoritesRequest getUserFavorites(@PathVariable String userMail){
        return this.appUserService.getUserFavorites(userMail);
    }

    @GetMapping("/getUserFavoriteLeagues/{userMail}")
    public Set<Long> getUserFavoritesLeagues(@PathVariable String userMail){
        return this.appUserService.getUserFavoritesLeagues(userMail);
    }

    @GetMapping("/getUserFavoriteTeams/{userMail}/{leagueId}")
    public Set<Long> getUserFavoriteTeams(@PathVariable String userMail, @PathVariable Long leagueId){
        return this.appUserService.getUserFavoriteTeams(userMail, leagueId);
    }

    @GetMapping("/getUserFavoriteGames/{userMail}/{leagueId}/{round}")
    public Set<Long> getUserFavoriteGames(@PathVariable String userMail, @PathVariable Long leagueId, @PathVariable int round){
        return this.appUserService.getUserFavoriteGames(userMail, leagueId, round);
    }
}
