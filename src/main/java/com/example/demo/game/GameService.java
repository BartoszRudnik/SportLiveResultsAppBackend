package com.example.demo.game;

import com.example.demo.appUser.AppUser;
import com.example.demo.appUser.AppUserRepository;
import com.example.demo.game.dto.*;
import com.example.demo.gameEvent.GameEvent;
import com.example.demo.gameEvent.GameEventRepository;
import com.example.demo.gamePlayer.GamePlayer;
import com.example.demo.gamePlayer.GamePlayerPosition;
import com.example.demo.gamePlayer.GamePlayerRepository;
import com.example.demo.gamePlayer.GamePlayerStatus;
import com.example.demo.gameStatistics.GameStatistics;
import com.example.demo.gameStatistics.GameStatisticsRepository;
import com.example.demo.league.League;
import com.example.demo.league.LeagueService;
import com.example.demo.league.dto.GameEventsResponse;
import com.example.demo.league.dto.GetGamesResponse;
import com.example.demo.player.*;
import com.example.demo.player.dto.GamePlayerResponse;
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

    public GameStatus getGameStatus(String nameOfStatus){
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

            if(game.getPlayers() == null){
                game.setPlayers(new HashSet<>());
            }

            List<SinglePlayerRequest> players = request.getPlayers();

            for(SinglePlayerRequest singlePlayerRequest : players){
                if(this.playerRepository.findById(singlePlayerRequest.getPlayerId()).isPresent()){
                    Player playerProfile = this.playerRepository.findById(singlePlayerRequest.getPlayerId()).get();
                    GamePlayer gamePlayer = new GamePlayer(playerProfile, game, this.getPlayerGameStatus(singlePlayerRequest.getPlayerStatus()), this.getPlayerGamePosition(singlePlayerRequest.getPlayerPosition()), singlePlayerRequest.getShirtNumber());

                    if(game.getPlayers().contains(gamePlayer)){
                        game.getPlayers().remove(gamePlayer);
                        playerProfile.removeGame(gamePlayer);

                        this.gamePlayerRepository.deleteByGameAndPlayer(game, gamePlayer.getPlayer());
                    }

                    game.addPlayer(gamePlayer);
                    playerProfile.addGame(gamePlayer);

                    this.gamePlayerRepository.save(gamePlayer);
                    this.playerRepository.save(playerProfile);
                }
            }
            this.gameRepository.save(game);
        }
    }

    private GamePlayerPosition getPlayerGamePosition(String playerPosition){
        if(playerPosition.equalsIgnoreCase("goalkeeper")){
            return GamePlayerPosition.GOALKEEPER;
        }else if(playerPosition.equalsIgnoreCase("left_defender")){
            return GamePlayerPosition.LEFT_DEFENDER;
        } else if(playerPosition.equalsIgnoreCase("center_defender")){
            return GamePlayerPosition.CENTER_DEFENDER;
        } else if(playerPosition.equalsIgnoreCase("right_defender")){
            return GamePlayerPosition.RIGHT_DEFENDER;
        }else if(playerPosition.equalsIgnoreCase("left_midfielder")){
            return GamePlayerPosition.LEFT_MIDFIELDER;
        }else if(playerPosition.equalsIgnoreCase("center_midfielder")){
            return GamePlayerPosition.CENTER_MIDFIELDER;
        }else if(playerPosition.equalsIgnoreCase("right_midfielder")){
            return GamePlayerPosition.RIGHT_MIDFIELDER;
        }else if(playerPosition.equalsIgnoreCase("left_forward")){
            return GamePlayerPosition.LEFT_FORWARD;
        }else if(playerPosition.equalsIgnoreCase("center_forward")){
            return GamePlayerPosition.CENTER_FORWARD;
        }else  if(playerPosition.equalsIgnoreCase("right_forward")){
            return GamePlayerPosition.RIGHT_FORWARD;
        }else{
            return GamePlayerPosition.BENCH;
        }
    }

    private GamePlayerStatus getPlayerGameStatus(String playerStatus){
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

            Set<GamePlayerResponse> squadTeamA = new HashSet<>();
            Set<GamePlayerResponse> squadTeamB = new HashSet<>();
            Set<GamePlayerResponse> substitutionsTeamA = new HashSet<>();
            Set<GamePlayerResponse> substitutionsTeamB = new HashSet<>();

            for(GamePlayer player : game.getPlayers()){
                if(Objects.equals(player.getPlayer().getTeam().getId(), game.getTeamA().getId())){
                    if(player.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD){
                        squadTeamA.add(new GamePlayerResponse(player.getPlayer().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber()));
                    }else{
                        substitutionsTeamA.add(new GamePlayerResponse(player.getPlayer().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber()));
                    }
                }else{
                    if(player.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD) {
                        squadTeamB.add(new GamePlayerResponse(player.getPlayer().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber()));
                    }else{
                        substitutionsTeamB.add(new GamePlayerResponse(player.getPlayer().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber()));
                    }
                }
            }

            return new GetLineupsResponse(squadTeamA, squadTeamB, substitutionsTeamA, substitutionsTeamB);
        }else{
            return new GetLineupsResponse();
        }
    }

    public GetGamesResponse getSingleGame(Long gameId) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();

            List<GamePlayerResponse> squadTeamA = new ArrayList<>();
            List<GamePlayerResponse> squadTeamB = new ArrayList<>();
            List<GamePlayerResponse> substitutionsTeamA = new ArrayList<>();
            List<GamePlayerResponse> substitutionsTeamB = new ArrayList<>();

            Set<GamePlayer> players = game.getPlayers();

            for(GamePlayer player : players){
                if(Objects.equals(player.getPlayer().getTeam().getId(), game.getTeamA().getId())){
                    if(player.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD){
                        squadTeamA.add(new GamePlayerResponse(player.getPlayer().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber()));
                    }else{
                        substitutionsTeamA.add(new GamePlayerResponse(player.getPlayer().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber()));
                    }
                }else{
                    if(player.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD) {
                        squadTeamB.add(new GamePlayerResponse(player.getPlayer().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber()));
                    }else{
                        substitutionsTeamB.add(new GamePlayerResponse(player.getPlayer().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber()));
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

            return new GetGamesResponse(game.getId(), game.getTeamA().getId(), game.getTeamB().getId(), game.getScoreTeamA(), game.getScoreTeamB(), game.getGameStartDate(), game.getActualStartDate(), game.getTeamA().getStadiumName(), gameEventsResponses, squadTeamA, squadTeamB, substitutionsTeamA, substitutionsTeamB, game.getRound(), game.isBreak(), game.getPartOfGame(), game.getLengthOfPartOfGame(), reporterMail, game.getGameStatus().toString());
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

    public int countTeamFinishedGames(Team team){
        return this.gameRepository.findTeamFinishedGames(team, GameStatus.FINISHED).size();
    }


    public List<Game> findTeamLiveAndScheduledGames(Team team){
        List<Game> games;

        games = this.gameRepository.findTeamLiveGames(team, GameStatus.IN_PROGRESS);
        games.addAll(this.gameRepository.findTeamScheduledGames(team, GameStatus.SCHEDULED).stream().filter((game -> game.getGameStartDate().isBefore(LocalDateTime.now().plusDays(8)))).collect(Collectors.toList()));

        return games;
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

    public List<SquadPlayerInfo> getSquadPlayerInfo(Long teamId) {
        List<SquadPlayerInfo> result = new ArrayList<>();
        List<Player> players = this.teamService.getTeam(teamId).getPlayers();

        for(Player player : players){
            if(player.getGames().size() > 0){
                GamePlayer latestGame = player.getGames().stream()
                        .sorted(Comparator.comparing((GamePlayer gamePlayer) -> gamePlayer.getGame().getGameStartDate()).reversed())
                        .collect(Collectors.toList()).get(0);

                result.add(new SquadPlayerInfo(player.getId(), latestGame.getShirtNumber(), latestGame.getGamePlayerPosition().toString()));
            }else{
                result.add(new SquadPlayerInfo(player.getId(), 1, "bench"));
            }
        }

        return result;
    }

    public Optional<Game> getGameOptional(Long gameId){
        return this.gameRepository.findById(gameId);
    }
}
