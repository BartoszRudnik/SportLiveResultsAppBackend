package com.example.demo.player;
import com.example.demo.game.Game;
import com.example.demo.gameEvent.GameEvent;
import com.example.demo.gameEvent.GameEventType;
import com.example.demo.gamePlayer.GamePlayer;
import com.example.demo.gamePlayer.GamePlayerRepository;
import com.example.demo.gamePlayer.GamePlayerStatus;
import com.example.demo.league.League;
import com.example.demo.league.LeagueRepository;
import com.example.demo.league.dto.GameEventsResponse;
import com.example.demo.player.dto.AddPlayerRequest;
import com.example.demo.player.dto.GetPlayerGamesResponse;
import com.example.demo.player.dto.SinglePlayerResponse;
import com.example.demo.player.dto.UpdatePlayerDataRequest;
import com.example.demo.team.Team;
import com.example.demo.team.TeamRepository;
import com.example.demo.team.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamService teamService;
    private final TeamRepository teamRepository;
    private final LeagueRepository leagueRepository;
    private final GamePlayerRepository gamePlayerRepository;

    public void addPlayer(AddPlayerRequest request){
        Team team = this.teamService.getTeam(request.getTeamId());
        Player newPlayer = new Player(request.getFirstName(), request.getLastName(), request.getPosition(), request.getNumberOfGoals(), request.getNumberOfAssists(), team);

        this.playerRepository.save(newPlayer);
    }

    public Player getPlayer(Long playerId){
        if(this.playerRepository.findById(playerId).isPresent()){
            return this.playerRepository.findById(playerId).get();
        }else{
            return null;
        }
    }

    public void updatePlayerTeam(Long playerId, Long newTeamId) {
        if(this.playerRepository.findById(playerId).isPresent() && this.teamRepository.findById(newTeamId).isPresent()){
            Player player = this.playerRepository.findById(playerId).get();
            Team newTeam = this.teamRepository.findById(newTeamId).get();

            player.setTeam(newTeam);

            this.playerRepository.save(player);
        }else{
            throw new IllegalStateException("Player or team doesn't exists");
        }
    }

    public void updatePlayerData(Long playerId, UpdatePlayerDataRequest request) {
        if(this.playerRepository.findById(playerId).isPresent()){
            Player player = this.playerRepository.findById(playerId).get();

            player.setFirstName(request.getFirstName());
            player.setLastName(request.getLastName());
            player.setPosition(request.getPosition());

            this.playerRepository.save(player);
        }else{
            throw new IllegalStateException("Player doesn't exist");
        }
    }

    public List<SinglePlayerResponse> getPlayerFromLeague(Long leagueId) {
        if(this.leagueRepository.findById(leagueId).isPresent()){
            League league = this.leagueRepository.findById(leagueId).get();

            List<Player> players = this.playerRepository.findAllByTeamLeague(league);
            List<SinglePlayerResponse> result = new ArrayList<>();

            for(Player player : players){
                result.add(new SinglePlayerResponse(player.getId(), player.getFirstName(), player.getLastName(), player.getPosition(), player.getNumberOfGoals(), player.getNumberOfAssists(), player.getTeam().getId()));
            }

            return result;
        }else{
            return new ArrayList<>();
        }
    }

    private int numberOfMinutesPlayed(Player player, Set<GamePlayer> gamePlayers, List<GameEvent> gameEvents){
        GamePlayer gamePlayer = gamePlayers.stream().filter(x -> x.getPlayer() == player).findFirst().orElse(null);

        if(gamePlayer != null){
            if(gamePlayer.getGamePlayerStatus() == GamePlayerStatus.INJURED){
                return 0;
            }
            else if(gamePlayer.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD){
                GameEvent gameEvent = gameEvents.stream().filter(x -> x.getPlayer() == player && x.getGameEventType() == GameEventType.SUBSTITUTION_OF).findFirst().orElse(null);

                if(gameEvent == null){
                    return 90;
                }else{
                    return gameEvent.getEventMinute();
                }
            }
            else{
                GameEvent gameEvent = gameEvents.stream().filter(x -> x.getPlayer() == player && x.getGameEventType() == GameEventType.SUBSTITUTION_ON).findFirst().orElse(null);

                if(gameEvent == null){
                    return 0;
                }else{
                    GameEvent secondGameEvent = gameEvents.stream().filter(x -> x.getPlayer() == player && x.getGameEventType() == GameEventType.SUBSTITUTION_OF).findFirst().orElse(null);

                    if(secondGameEvent == null){
                        return 90 - gameEvent.getEventMinute();
                    }else{
                        return secondGameEvent.getEventMinute() - gameEvent.getEventMinute();
                    }
                }
            }
        }else{
            return 0;
        }
    }

    public List<GetPlayerGamesResponse> getPlayerGames(Long playerId) {
        if(this.playerRepository.findById(playerId).isPresent()){
            Player mainPlayer = this.playerRepository.findById(playerId).get();
            List<GamePlayer> playerGames = this.gamePlayerRepository.findAllByPlayer(mainPlayer);
            List<GetPlayerGamesResponse> response = new ArrayList<>();

            for(GamePlayer playerGame : playerGames){
                Game game = playerGame.getGame();

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

                int numberOfGoals = 0;
                boolean isYellowCard = false;
                boolean isRedCard = false;
                int numberOfMinutes = this.numberOfMinutesPlayed(mainPlayer, players, gameEvents);

                for(GameEvent gameEvent : gameEvents){
                    if(gameEvent.getPlayer() == mainPlayer){
                        if(gameEvent.getGameEventType() == GameEventType.GOAL){
                            numberOfGoals++;
                        }else if(gameEvent.getGameEventType() == GameEventType.RED_CARD){
                            isRedCard = true;
                        }else if(gameEvent.getGameEventType() == GameEventType.YELLOW_CARD){
                            isYellowCard = true;
                        }
                    }

                    gameEventsResponses.add(new GameEventsResponse(gameEvent.getEventMinute(), gameEvent.getPlayer().getId(), gameEvent.getTeam().getId(), gameEvent.getGameEventType().toString()));
                }

                response.add(new GetPlayerGamesResponse(game.getId(), game.getTeamA().getId(), game.getTeamB().getId(), game.getScoreTeamA(), game.getScoreTeamB(), game.getGameStartDate(), game.getTeamA().getStadiumName(), gameEventsResponses, squadTeamA, squadTeamB, substitutionsTeamA, substitutionsTeamB, game.getRound(), numberOfMinutes, numberOfGoals, isYellowCard, isRedCard));
            }

            return response;
        }else{
            return new ArrayList<>();
        }
    }
}
