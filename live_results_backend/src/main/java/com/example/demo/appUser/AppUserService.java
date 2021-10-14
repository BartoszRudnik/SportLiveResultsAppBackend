package com.example.demo.appUser;

import com.example.demo.appUser.dto.UserFavoritesRequest;
import com.example.demo.confirmationToken.ConfirmationToken;
import com.example.demo.confirmationToken.ConfirmationTokenService;
import com.example.demo.game.Game;
import com.example.demo.game.GameRepository;
import com.example.demo.league.League;
import com.example.demo.league.LeagueRepository;
import com.example.demo.signUp.dto.SignUpRequest;
import com.example.demo.team.Team;
import com.example.demo.team.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with email %s not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;
    private final LeagueRepository leagueRepository;

    public Optional<AppUser> findByEmail(String email){
        return this.appUserRepository.findByEmail(email);
    }

    public void deleteUser(AppUser appUser){
        this.appUserRepository.delete(appUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.appUserRepository.
                findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public ConfirmationToken signInUser(String email, String password){

        boolean userExists = this.appUserRepository.
                findByEmail(email).
                isPresent();

        if(!userExists){
            throw new IllegalStateException("User doesn't exist");
        }

        AppUser appUser = this.appUserRepository.findByEmail(email).get();

        if(!this.bCryptPasswordEncoder.matches(password, appUser.getPassword()) || !appUser.getEnabled()){
            throw new IllegalStateException("Wrong email or password");
        }

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        this.confirmationTokenService.updateConfirmationToken(confirmationToken);

        return confirmationToken;
    }

    public ConfirmationToken googleSignIn(SignUpRequest request){

        boolean userExists = this.appUserRepository
                .findByEmail(request.getEmail())
                .isPresent();

        if(!userExists){
            AppUserRole appUserRole;

            if(request.getUserType().equals("google")){
                appUserRole = AppUserRole.GOOGLE_USER;
            }else{
                appUserRole = AppUserRole.MAIL_USER;
            }

            AppUser appUser = new AppUser(request.getFirstName(), request.getLastName(), request.getEmail(), appUserRole);

            this.appUserRepository.save(appUser);
        }

        AppUser appUser = this.appUserRepository.findByEmail(request.getEmail()).get();

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
          token,
          LocalDateTime.now(),
          LocalDateTime.now().plusMinutes(15),
                appUser
        );

        this.confirmationTokenService.updateConfirmationToken(confirmationToken);

        return confirmationToken;
    }

    public ConfirmationToken signUpUser(AppUser appUser){

        boolean userExists = this.appUserRepository.findByEmail(appUser.getEmail()).isPresent();

        if(userExists){
            throw new IllegalStateException("Email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        this.appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
          token,
          LocalDateTime.now(),
          LocalDateTime.now().plusMinutes(15),
                appUser
        );

        this.confirmationTokenService.saveConfirmationToken(confirmationToken);

        return confirmationToken;
    }

    public void enableUser(String email){
        this.appUserRepository.enableAppUser(email);
    }

    public void disableUser(String email){
        this.appUserRepository.disableAppUser(email);
    }

    public void updateUserPassword(String email, String newPassword){
        this.appUserRepository.updateAppUserPassword(email, newPassword);
    }

    public void changeTeamFavoriteStatus(String userMail, Long teamId) {
        if(this.appUserRepository.findByEmail(userMail).isPresent() && this.teamRepository.findById(teamId).isPresent()){

            AppUser appUser = this.appUserRepository.findByEmail(userMail).get();
            Team team = this.teamRepository.findById(teamId).get();

            if(appUser.getFavoriteTeams() != null && appUser.getFavoriteTeams().contains(team)){
                appUser.removeFavoriteTeam(team);
            }else{
                appUser.addFavoriteTeam(team);
            }

            this.appUserRepository.save(appUser);
        }
    }

    public void changeGameFavoriteStatus(String userMail, Long gameId) {
        if(this.appUserRepository.findByEmail(userMail).isPresent() && this.gameRepository.findById(gameId).isPresent()){

            AppUser appUser = this.appUserRepository.findByEmail(userMail).get();
            Game game = this.gameRepository.findById(gameId).get();

            if(appUser.getFavoriteGames() != null && appUser.getFavoriteGames().contains(game)){
                appUser.removeFavoriteGame(game);
            }else{
                appUser.addFavoriteGame(game);
            }

            this.appUserRepository.save(appUser);
        }
    }

    public void changeLeagueFavoriteStatus(String userMail, Long leagueId) {
        if(this.appUserRepository.findByEmail(userMail).isPresent() && this.leagueRepository.findById(leagueId).isPresent()){

            AppUser appUser = this.appUserRepository.findByEmail(userMail).get();
            League league = this.leagueRepository.findById(leagueId).get();

            if(appUser.getFavoriteLeagues() != null && appUser.getFavoriteLeagues().contains(league)){
                appUser.removeFavoriteLeague(league);
            }else{
                appUser.addFavoriteLeague(league);
            }

            this.appUserRepository.save(appUser);
        }
    }

    public UserFavoritesRequest getUserFavorites(String userMail) {
        if(this.appUserRepository.findByEmail(userMail).isPresent()){
            AppUser user = this.appUserRepository.findByEmail(userMail).get();

            List<Long> games = new ArrayList<>();
            List<Long> leagues = new ArrayList<>();
            List<Long> teams = new ArrayList<>();

            Set<Game> userGames = user.getFavoriteGames();
            Set<League> userLeagues = user.getFavoriteLeagues();
            Set<Team> userTeams = user.getFavoriteTeams();

            for(Game game : userGames){
                games.add(game.getId());
            }

            for(League league : userLeagues){
                leagues.add(league.getId());
            }

            for(Team team : userTeams){
                teams.add(team.getId());
            }

            return new UserFavoritesRequest(games, teams, leagues);
        }else{
            return null;
        }
    }

    public Set<Long> getUserFavoritesLeagues(String userMail) {
        if(this.appUserRepository.findByEmail(userMail).isPresent()){
            AppUser appUser = this.appUserRepository.findByEmail(userMail).get();

            Set<League> favoriteLeagues = appUser.getFavoriteLeagues();
            Set<Long> result = new HashSet<>();

            for(League league : favoriteLeagues){
                result.add(league.getId());
            }

            return result;
        }else{
            return new HashSet<>();
        }
    }

    public Set<Long> getUserFavoriteGames(String userMail, Long leagueId, int round){
        if(this.appUserRepository.findByEmail(userMail).isPresent()){
            AppUser appUser = this.appUserRepository.findByEmail(userMail).get();

            Set<Game> favoriteGames = appUser.getFavoriteGames();
            Set<Long> result = new HashSet<>();

            for(Game game : favoriteGames){
                if(Objects.equals(game.getLeague().getId(), leagueId) && Objects.equals(game.getRound(), round)) {
                    result.add(game.getId());
                }
            }

            return result;
        }else{
            return new HashSet<>();
        }
    }

    public Set<Long> getUserFavoriteTeams(String userMail, Long leagueId) {
        if(this.appUserRepository.findByEmail(userMail).isPresent()){
            AppUser appUser = this.appUserRepository.findByEmail(userMail).get();

            Set<Team> favoriteTeams = appUser.getFavoriteTeams();
            Set<Long> result = new HashSet<>();

            for(Team team : favoriteTeams){
                if(Objects.equals(team.getLeague().getId(), leagueId)) {
                    result.add(team.getId());
                }
            }

            return result;
        }else{
            return new HashSet<>();
        }
    }
}
