package com.example.demo.appUser;

import com.example.demo.appUser.dto.GetFavoriteGamesResponse;
import com.example.demo.appUser.dto.UserFavoritesRequest;
import com.example.demo.league.dto.GetLeaguesResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PostMapping("/activateGameNotification/{userMail}/{gameId}")
    public void activateGameNotification(@PathVariable String userMail, @PathVariable Long gameId){
        this.appUserService.activateGameNotification(userMail, gameId);
    }

    @PostMapping("/deactivateGameNotification/{userMail}/{gameId}")
    public void deactivateGameNotification(@PathVariable String userMail, @PathVariable Long gameId){
        this.appUserService.deactivateGameNotification(userMail, gameId);
    }

    @PostMapping("/changeGameNotificationStatus/{userMail}/{gameId}")
    public void changeGameNotificationStatus(@PathVariable String userMail, @PathVariable Long gameId){
        this.appUserService.changeGameNotificationStatus(userMail, gameId);
    }

    @GetMapping("/getUserFavorites/{userMail}")
    public UserFavoritesRequest getUserFavorites(@PathVariable String userMail){
        return this.appUserService.getUserFavorites(userMail);
    }

    @GetMapping("/getUserFavoriteLeagues/{userMail}")
    public List<GetLeaguesResponse> getUserFavoritesLeagues(@PathVariable String userMail){
        return this.appUserService.getUserFavoritesLeagues(userMail);
    }

    @GetMapping("/getUserFavoriteLeaguesId/{userMail}")
    public Set<Long> getUserFavoritesLeaguesId(@PathVariable String userMail){
        return this.appUserService.getUserFavoritesLeaguesId(userMail);
    }

    @GetMapping("/getUserFavoriteTeams/{userMail}/{leagueId}")
    public Set<Long> getUserFavoriteTeams(@PathVariable String userMail, @PathVariable Long leagueId){
        return this.appUserService.getUserFavoriteTeams(userMail, leagueId);
    }
    
    @GetMapping("/getUserAllNotificationGames/{userMail}")
    public Set<Long> getUserAllNotificationGames(@PathVariable String userMail){
        return this.appUserService.getUserAllNotificationGames(userMail);
    }

    @GetMapping("/getUserNotificationGames/{userMail}/{leagueId}/{round}")
    public Set<Long> getUserNotificationGames(@PathVariable String userMail, @PathVariable Long leagueId, @PathVariable int round){
        return this.appUserService.getUserNotificationGames(userMail, leagueId, round);
    }

    @GetMapping("/getUserAllFavoritesGames/{userMail}")
    public Set<Long> getUserAllFavoritesGames(@PathVariable String userMail){
        return this.appUserService.getUserAllFavoritesGames(userMail);
    }

    @GetMapping("/getUserFavoriteGames/{userMail}/{leagueId}/{round}")
    public Set<Long> getUserFavoriteGames(@PathVariable String userMail, @PathVariable Long leagueId, @PathVariable int round){
        return this.appUserService.getUserFavoriteGames(userMail, leagueId, round);
    }

    @GetMapping("/getAllFavoriteUserTeamGames/{userMail}")
    public List<GetFavoriteGamesResponse> getFavoriteGamesResponses(@PathVariable String userMail){
        return this.appUserService.getAllFavoriteUserTeamGames(userMail);
    }

    @GetMapping("/getAllFavoriteUserGames/{userMail}")
    public List<GetFavoriteGamesResponse> getAllFavoriteUserGames(@PathVariable String userMail){
        return this.appUserService.getAllFavoriteUserGames(userMail);
    }
}
