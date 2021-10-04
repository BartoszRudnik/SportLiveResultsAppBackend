package com.example.demo.appUser;

import com.example.demo.appUser.dto.UserFavoritesRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
