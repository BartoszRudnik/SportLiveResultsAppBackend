package com.example.demo.game;

import com.example.demo.appUser.AppUser;
import com.example.demo.appUser.AppUserRepository;
import com.example.demo.game.dto.*;
import com.example.demo.gameEvent.GameEvent;
import com.example.demo.gameEvent.GameEventRepository;
import com.example.demo.gamePlayer.GamePlayer;
import com.example.demo.gamePlayer.GamePlayerRepository;
import com.example.demo.gamePlayer.GamePlayerStatus;
import com.example.demo.gameStatistics.GameStatistics;
import com.example.demo.gameStatistics.GameStatisticsRepository;
import com.example.demo.league.League;
import com.example.demo.league.LeagueService;
import com.example.demo.league.dto.GameEventsResponse;
import com.example.demo.league.dto.GetGamesResponse;
import com.example.demo.player.*;
import com.example.demo.team.Team;
import com.example.demo.team.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GameService {
    private final GameRepository gameRepository;
    private final LeagueService leagueService;
    private final TeamService teamService;
    private final PlayerRepository playerRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final GameEventRepository gameEventRepository;
    private final GameStatisticsRepository gameStatisticsRepository;
    private final AppUserRepository appUserRepository;

    public Game getGame(Long gameId) {
        if (this.gameRepository.findById(gameId).isPresent()){
            return this.gameRepository.findById(gameId).get();
        }else{
            return null;
        }
    }

    public GameEventsResponse getGameEventsResponse(Long gameEventId){
        if(this.gameEventRepository.findById(gameEventId).isPresent()){
            GameEvent event = this.gameEventRepository.findById(gameEventId).get();

            return new GameEventsResponse(event.getId(), event.getEventMinute(), event.getPlayer().getId(), event.getTeam().getId(), event.getGameEventType().toString());
        }else{
            return new GameEventsResponse();
        }
    }

    public List<GameEvent> getGameEvents(Long gameId){
        if(this.checkIfGameNotExist(gameId)){
            throw new IllegalStateException("Game doesn't exist");
        }

        return this.gameRepository.findById(gameId).get().getGameEvents();
    }

    public void addGameEvent(Long gameId, GameEvent newGameEvent){
        if(this.checkIfGameNotExist(gameId)){
            throw new IllegalStateException("Game doesn't exist");
        }

        Game game = this.gameRepository.findById(gameId).get();

        game.addGameEvent(newGameEvent);

        this.gameRepository.save(game);
    }

    private boolean checkIfGameNotExist(Long gameId){
        return this.gameRepository.findById(gameId).isEmpty();
    }

    public Long addGame(AddGameRequest request) {
        League league;
        Team teamA;
        Team teamB;
        GameStatus gameStatus;

        league = this.leagueService.findById(request.getLeagueId());
        teamA = this.teamService.getTeam(request.getTeamAId());
        teamB = this.teamService.getTeam(request.getTeamBId());
        gameStatus = this.getGameStatus(request.getGameStatus());

        GameStatistics statistics = new GameStatistics();

        Game game =
                new Game(0, 0, league, teamA, teamB, request.getGameStartDate(), request.getGameStartDate(), gameStatus, request.getRound(), statistics);

        this.gameStatisticsRepository.save(statistics);
       this.gameRepository.save(game);

       return game.getId();
    }

    private GameStatus getGameStatus(String nameOfStatus){
        if(nameOfStatus.equalsIgnoreCase("SCHEDULED")){
            return GameStatus.SCHEDULED;
        }else if(nameOfStatus.equalsIgnoreCase("IN_PROGRESS")){
            return GameStatus.IN_PROGRESS;
        }else if(nameOfStatus.equalsIgnoreCase("FINISHED")){
            return GameStatus.FINISHED;
        }else{
            return GameStatus.POSTPONED;
        }
    }

    public void updateScore(Long gameId, UpdateScoreRequest request) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();

            game.setScoreTeamA(request.getScoreTeamA());
            game.setScoreTeamB(request.getScoreTeamB());

            this.gameRepository.save(game);
        }else{
            throw new IllegalStateException("Game doesn't exist");
        }
    }

    public void changeGameStatus(Long gameId, String statusName) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();

            GameStatus newStatus = this.getGameStatus(statusName);
            game.setGameStatus(newStatus);

            this.gameRepository.save(game);
        }else{
            throw new IllegalStateException("Game doesn't exist");
        }
    }

    public void addLineups(Long gameId, AddLineupsRequest request) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();

            if(game.getPlayers() != null){
                game.setPlayers(game.getPlayers().stream().filter(player -> player.getPlayer().getTeam().getId() != request.getTeamId()).collect(Collectors.toSet()));
            }else{
                game.setPlayers(new HashSet<>());
            }

            List<SinglePlayerRequest> players = request.getPlayers();

            for(SinglePlayerRequest singlePlayerRequest : players){
                if(this.playerRepository.findById(singlePlayerRequest.getPlayerId()).isPresent()){
                    Player newPlayer = this.playerRepository.findById(singlePlayerRequest.getPlayerId()).get();
                    GamePlayer newGamePlayer = new GamePlayer(newPlayer, game, this.getPlayerGameStatus(singlePlayerRequest.getPlayerStatus()));

                    game.addPlayer(newGamePlayer);
                    newPlayer.addGame(newGamePlayer);

                    this.gamePlayerRepository.save(newGamePlayer);
                    this.playerRepository.save(newPlayer);
                }
            }
            this.gameRepository.save(game);
        }
    }

    private GamePlayerStatus getPlayerGameStatus(String playerStatus) {
        if(playerStatus.equalsIgnoreCase("INJURED")){
            return GamePlayerStatus.INJURED;
        }else if(playerStatus.equalsIgnoreCase("FIRST_SQUAD")){
            return GamePlayerStatus.FIRST_SQUAD;
        }else{
            return GamePlayerStatus.SUBSTITUTION;
        }
    }

    public GetLineupsResponse getLineups(Long gameId) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();

            Team teamA = game.getTeamA();
            Set<GamePlayer> gamePlayers = game.getPlayers();

            Set<Player> teamAPlayers = new HashSet<>();
            Set<Player> teamBPlayers = new HashSet<>();

            for(GamePlayer player : gamePlayers){
                if(player.getPlayer().getTeam() == teamA){
                    teamAPlayers.add(player.getPlayer());
                }else{
                    teamBPlayers.add(player.getPlayer());
                }
            }

            return new GetLineupsResponse(teamAPlayers, teamBPlayers);
        }else{
            return new GetLineupsResponse();
        }
    }

    public GetGamesResponse getSingleGame(Long gameId) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();

            List<Long> squadTeamA = new ArrayList<>();
            List<Long> squadTeamB = new ArrayList<>();
            List<Long> substitutionsTeamA = new ArrayList<>();
            List<Long> substitutionsTeamB = new ArrayList<>();

            Set<GamePlayer> players = game.getPlayers();

            for(GamePlayer player : players){
                if(Objects.equals(player.getPlayer().getTeam().getId(), game.getTeamA().getId())){
                    if(player.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD){
                        squadTeamA.add(player.getPlayer().getId());
                    }else{
                        substitutionsTeamA.add(player.getPlayer().getId());
                    }
                }else{
                    if(player.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD) {
                        squadTeamB.add(player.getPlayer().getId());
                    }else{
                        substitutionsTeamB.add(player.getPlayer().getId());
                    }
                }
            }

            List<GameEvent> gameEvents = game.getGameEvents();
            List<GameEventsResponse> gameEventsResponses = new ArrayList<>();

            for(GameEvent gameEvent : gameEvents){
                gameEventsResponses.add(new GameEventsResponse(gameEvent.getId(), gameEvent.getEventMinute(), gameEvent.getPlayer().getId(), gameEvent.getTeam().getId(), gameEvent.getGameEventType().toString()));
            }

            String reporterMail = "";

            if(game.getReporter() != null){
                reporterMail = game.getReporter().getEmail();
            }

            return new GetGamesResponse(game.getId(), game.getTeamA().getId(), game.getTeamB().getId(), game.getScoreTeamA(), game.getScoreTeamB(), game.getGameStartDate(), game.getActualStartDate(), game.getTeamA().getStadiumName(), gameEventsResponses, squadTeamA, squadTeamB, substitutionsTeamA, substitutionsTeamB, game.getRound(), game.isBreak(), game.getPartOfGame(), game.getLengthOfPartOfGame(), reporterMail);
        }else{
            return new GetGamesResponse();
        }
    }

    public void addReporter(String userMail, Long gameId) {
        if(this.appUserRepository.findByEmail(userMail).isPresent() && this.gameRepository.findById(gameId).isPresent()){
            AppUser appUser = this.appUserRepository.findByEmail(userMail).get();
            Game game = this.gameRepository.findById(gameId).get();

            game.setReporter(appUser);

            this.gameRepository.save(game);
        }
    }

    public void removeReporter(Long gameId) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();

            game.setReporter(null);

            this.gameRepository.save(game);
        }
    }

    public boolean isReporter(Long gameId) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();

            return game.getReporter() != null;
        }else{
            return false;
        }
    }

    public String getReporter(Long gameId) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();

            if(game.getReporter() != null){
                return game.getReporter().getEmail();
            }else{
                return "";
            }
        }else{
            return "";
        }
    }
}
