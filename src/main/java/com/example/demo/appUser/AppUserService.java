package com.example.demo.appUser;

import com.example.demo.appUser.dto.GetFavoriteGamesResponse;
import com.example.demo.appUser.dto.UserFavoritesRequest;
import com.example.demo.confirmationToken.ConfirmationToken;
import com.example.demo.confirmationToken.ConfirmationTokenService;
import com.example.demo.game.Game;
import com.example.demo.game.GameRepository;
import com.example.demo.game.GameService;
import com.example.demo.game.GameStatus;
import com.example.demo.gameEvent.GameEvent;
import com.example.demo.gameEvent.GameEventType;
import com.example.demo.gamePlayer.GamePlayer;
import com.example.demo.gamePlayer.GamePlayerStatus;
import com.example.demo.league.League;
import com.example.demo.league.LeagueRepository;
import com.example.demo.league.dto.GameEventsResponse;
import com.example.demo.league.dto.GetLeaguesResponse;
import com.example.demo.player.dto.SinglePlayerResponse;
import com.example.demo.signIn.dto.SignInAnon;
import com.example.demo.signUp.dto.SignUpRequest;
import com.example.demo.team.Team;
import com.example.demo.team.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final GameService gameService;

    public Optional<AppUser> findByEmail(String email){
        return this.appUserRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user =  this.appUserRepository.
                findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(user.getAppUserRole().name()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
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

    public ConfirmationToken anonSignIn(SignInAnon request) {
        boolean userExists = this.appUserRepository.findByEmail(request.getUuid()).isPresent();

        if(!userExists){
            AppUserRole appUserRole = AppUserRole.ANON;
            AppUser appUser = new AppUser("", "", request.getUuid(), appUserRole);

            this.appUserRepository.save(appUser);
        }

        AppUser appUser = this.appUserRepository.findByEmail(request.getUuid()).get();

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

    public void activateGameNotification(String userMail, Long gameId) {
        if(this.appUserRepository.findByEmail(userMail).isPresent() && this.gameRepository.findById(gameId).isPresent()){
            AppUser appUser = this.appUserRepository.findByEmail(userMail).get();
            Game game = this.gameRepository.findById(gameId).get();

            appUser.addNotificationGame(game);

            this.appUserRepository.save(appUser);
        }
    }

    public void deactivateGameNotification(String userMail, Long gameId) {
        if(this.appUserRepository.findByEmail(userMail).isPresent() && this.gameRepository.findById(gameId).isPresent()){
            AppUser appUser = this.appUserRepository.findByEmail(userMail).get();
            Game game = this.gameRepository.findById(gameId).get();

            appUser.removeNotificationGame(game);

            this.appUserRepository.save(appUser);
        }
    }

    public void changeGameNotificationStatus(String userMail, Long gameId) {
        if(this.appUserRepository.findByEmail(userMail).isPresent() && this.gameRepository.findById(gameId).isPresent()){

            AppUser appUser = this.appUserRepository.findByEmail(userMail).get();
            Game game = this.gameRepository.findById(gameId).get();

            if(appUser.getNotificationGames() != null && appUser.getNotificationGames().contains(game)){
                appUser.removeNotificationGame(game);
            }else{
                appUser.addNotificationGame(game);
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

    public List<GetLeaguesResponse> getUserFavoritesLeagues(String userMail) {
        if(this.appUserRepository.findByEmail(userMail).isPresent()){
            AppUser appUser = this.appUserRepository.findByEmail(userMail).get();

            Set<League> favoriteLeagues = appUser.getFavoriteLeagues();
            List<GetLeaguesResponse> result = new ArrayList<>();

            for(League league : favoriteLeagues){
                result.add(new GetLeaguesResponse(league.getId(), league.getLeagueName(), league.getLeagueLevel().toString()));
            }

            return result;
        }else{
            return new ArrayList<>();
        }
    }

    public Set<Long> getUserFavoritesLeaguesId(String userMail) {
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

    public Set<Long> getUserNotificationGames(String userMail, Long leagueId, int round) {
        if(this.appUserRepository.findByEmail(userMail).isPresent()){
            AppUser appUser = this.appUserRepository.findByEmail(userMail).get();

            Set<Game> notificationGames = appUser.getNotificationGames();
            Set<Long> result = new HashSet<>();

            for(Game game : notificationGames){
                if(Objects.equals(game.getLeague().getId(), leagueId) && Objects.equals(game.getRound(), round)) {
                    result.add(game.getId());
                }
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

    public Set<Long> getUserAllFavoritesGames(String userMail) {
        if(this.appUserRepository.findByEmail(userMail).isPresent()){
            Set<Game> favoriteGames = this.appUserRepository.findByEmail(userMail).get().getFavoriteGames();
            Set<Long> result = new HashSet<>();

            for(Game game : favoriteGames){
                result.add(game.getId());
            }

            return result;
        }else{
            return new HashSet<>();
        }
    }


    public Set<Long> getUserAllNotificationGames(String userMail) {
        if(this.appUserRepository.findByEmail(userMail).isPresent()){
            Set<Game> notificationGames = this.appUserRepository.findByEmail(userMail).get().getNotificationGames();
            Set<Long> result = new HashSet<>();

            for(Game game : notificationGames){
                result.add(game.getId());
            }

            return result;
        }
        return new HashSet<>();
    }

    private List<GetFavoriteGamesResponse> getGamesWithFullPlayerInfo(Set<Game> games, Set<Game> notificationGames){
        List<GetFavoriteGamesResponse> result = new ArrayList<>();

        for(Game game : games){
            List<SinglePlayerResponse> squadTeamA = new ArrayList<>();
            List<SinglePlayerResponse> squadTeamB = new ArrayList<>();
            List<SinglePlayerResponse> substitutionsTeamA = new ArrayList<>();
            List<SinglePlayerResponse> substitutionsTeamB = new ArrayList<>();

            for(GamePlayer player : game.getPlayers()){
                int numberOfGoals = (int) player.getPlayer().getGameEvents().stream().filter(event -> event.getGameEventType() == GameEventType.GOAL).count();
                int numberOfRed = (int) player.getPlayer().getGameEvents().stream().filter(event -> event.getGameEventType() == GameEventType.RED_CARD).count();
                int numberOfYellow = (int) player.getPlayer().getGameEvents().stream().filter(event -> event.getGameEventType() == GameEventType.YELLOW_CARD).count();

                if(Objects.equals(player.getPlayer().getTeam().getId(), game.getTeamA().getId())){
                    if(player.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD){
                        squadTeamA.add(new SinglePlayerResponse(player.getPlayer().getId(), player.getPlayer().getFirstName(), player.getPlayer().getLastName(), player.getPlayer().getPosition(), numberOfGoals, 0, player.getPlayer().getTeam().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber(), numberOfRed
                        , numberOfYellow));
                    }else{
                        substitutionsTeamA.add(new SinglePlayerResponse(player.getPlayer().getId(), player.getPlayer().getFirstName(), player.getPlayer().getLastName(), player.getPlayer().getPosition(), numberOfGoals, 0, player.getPlayer().getTeam().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber(), numberOfRed, numberOfYellow));
                    }
                }else{
                    if(player.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD) {
                        squadTeamB.add(new SinglePlayerResponse(player.getPlayer().getId(), player.getPlayer().getFirstName(), player.getPlayer().getLastName(), player.getPlayer().getPosition(), numberOfGoals, 0, player.getPlayer().getTeam().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber(), numberOfRed, numberOfYellow));
                    }else{
                        substitutionsTeamB.add(new SinglePlayerResponse(player.getPlayer().getId(), player.getPlayer().getFirstName(), player.getPlayer().getLastName(), player.getPlayer().getPosition(), numberOfGoals, 0, player.getPlayer().getTeam().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber(), numberOfRed, numberOfYellow));
                    }
                }
            }

            List<GameEventsResponse> gameEventsResponses = new ArrayList<>();

            for(GameEvent gameEvent : game.getGameEvents()){
                gameEventsResponses.add(new GameEventsResponse(gameEvent.getId(), gameEvent.getEventMinute(), gameEvent.getPlayer().getId(), gameEvent.getTeam().getId(), gameEvent.getGameEventType().toString()));
            }

            String reporterMail = "";

            if(game.getReporter() != null){
                reporterMail = game.getReporter().getEmail();
            }

            boolean isNotification = notificationGames.contains(game);

            result.add(new GetFavoriteGamesResponse(game.getId(), game.getLeague().getId(), game.getTeamA().getId(), game.getTeamB().getId(), game.getScoreTeamA(), game.getScoreTeamB(), game.getGameStartDate(), game.getActualStartDate(), game.getTeamA().getStadiumName(), gameEventsResponses, squadTeamA, squadTeamB, substitutionsTeamA, substitutionsTeamB, game.getRound(), game.isBreak(), game.getPartOfGame(), game.getLengthOfPartOfGame(), reporterMail, isNotification, game.getGameStatus().toString()));
        }

        return result;
    }

    public List<GetFavoriteGamesResponse> getAllFavoriteUserTeamGames(String userMail) {
        if(this.appUserRepository.findByEmail(userMail).isPresent()){
            AppUser appUser = this.appUserRepository.findByEmail(userMail).get();

            Set<Team> favoriteTeams = appUser.getFavoriteTeams();

            Set<Game> games = new HashSet<>();

            for(Team team : favoriteTeams){
                games.addAll(this.gameService.findTeamLiveAndScheduledGames(team));
            }

            return this.getGamesWithFullPlayerInfo(games, appUser.getNotificationGames());
        }else{
            return new ArrayList<>();
        }
    }

    public List<GetFavoriteGamesResponse> getAllFavoriteUserGames(String userMail) {
        if(this.appUserRepository.findByEmail(userMail).isPresent()) {
            AppUser appUser = this.appUserRepository.findByEmail(userMail).get();
            Set<Game> favoriteUserGames = appUser.getFavoriteGames().stream().filter(game -> game.getGameStatus() == GameStatus.IN_PROGRESS || game.getGameStatus() == GameStatus.SCHEDULED).collect(Collectors.toSet());

            return this.getGamesWithFullPlayerInfo(favoriteUserGames, appUser.getNotificationGames());
        }else{
            return new ArrayList<>();
        }
    }
}
