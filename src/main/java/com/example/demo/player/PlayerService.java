package com.example.demo.player;

import com.example.demo.game.Game;
import com.example.demo.game.GameService;
import com.example.demo.game.GameStatus;
import com.example.demo.gameEvent.GameEvent;
import com.example.demo.gameEvent.GameEventType;
import com.example.demo.gamePlayer.GamePlayer;
import com.example.demo.gamePlayer.GamePlayerRepository;
import com.example.demo.gamePlayer.GamePlayerStatus;
import com.example.demo.league.League;
import com.example.demo.league.LeagueRepository;
import com.example.demo.league.dto.GameEventsResponse;
import com.example.demo.player.dto.*;
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
    private final GameService gameService;

    public void addPlayer(AddPlayerRequest request) {
        Team team = this.teamService.getTeam(request.getTeamId());
        Player newPlayer = new Player(request.getFirstName(), request.getLastName(), request.getPosition(), request.getNumberOfAssists(), team);

        this.playerRepository.save(newPlayer);
    }

    public Player getPlayer(Long playerId) {
        if (this.playerRepository.findById(playerId).isPresent()) {
            return this.playerRepository.findById(playerId).get();
        } else {
            return null;
        }
    }

    public void updatePlayerTeam(Long playerId, Long newTeamId) {
        if (this.playerRepository.findById(playerId).isPresent() && this.teamRepository.findById(newTeamId).isPresent()) {
            Player player = this.playerRepository.findById(playerId).get();
            Team newTeam = this.teamRepository.findById(newTeamId).get();

            player.setTeam(newTeam);

            this.playerRepository.save(player);
        } else {
            throw new IllegalStateException("Player or team doesn't exists");
        }
    }

    public void updatePlayerData(Long playerId, UpdatePlayerDataRequest request) {
        if (this.playerRepository.findById(playerId).isPresent()) {
            Player player = this.playerRepository.findById(playerId).get();

            player.setFirstName(request.getFirstName());
            player.setLastName(request.getLastName());
            player.setPosition(request.getPosition());

            this.playerRepository.save(player);
        } else {
            throw new IllegalStateException("Player doesn't exist");
        }
    }

    public SinglePlayerResponse getSinglePlayer(Long playerId) {
        if (this.playerRepository.findById(playerId).isPresent()) {
            Player player = this.playerRepository.findById(playerId).get();

            int numberOfGoals = (int) player.getGameEvents().stream().filter(event -> event.getGameEventType() == GameEventType.GOAL).count();

            return new SinglePlayerResponse(player.getId(), player.getFirstName(), player.getLastName(), player.getPosition(), numberOfGoals, player.getNumberOfAssists(), player.getTeam().getId(), "", 0);
        } else {
            return new SinglePlayerResponse();
        }
    }

    public List<SinglePlayerResponse> getPlayerFromLeague(Long leagueId) {
        if (this.leagueRepository.findById(leagueId).isPresent()) {
            League league = this.leagueRepository.findById(leagueId).get();

            List<Player> players = this.playerRepository.findAllByTeamLeague(league);
            List<SinglePlayerResponse> result = new ArrayList<>();

            for (Player player : players) {
                int numberOfGoals = (int) player.getGameEvents().stream().filter(event -> event.getGameEventType() == GameEventType.GOAL).count();

                result.add(new SinglePlayerResponse(player.getId(), player.getFirstName(), player.getLastName(), player.getPosition(), numberOfGoals, player.getNumberOfAssists(), player.getTeam().getId(), "", 0));
            }

            return result;
        } else {
            return new ArrayList<>();
        }
    }

    private Long numberOfMinutesPlayed(Player player, GamePlayer gamePlayer) {
        if (gamePlayer.getGamePlayerStatus() == GamePlayerStatus.INJURED) {
            return 0L;
        } else if (gamePlayer.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD) {
            GameEvent gameEvent = gamePlayer.getGame().getGameEvents().stream().filter(x -> x.getPlayer() == player && x.getGameEventType() == GameEventType.SUBSTITUTION_OF).findFirst().orElse(null);

            if (gameEvent == null) {
                return 90L;
            } else {
                return gameEvent.getEventMinute();
            }
        } else {
            GameEvent gameEvent = gamePlayer.getGame().getGameEvents().stream().filter(x -> x.getPlayer() == player && x.getGameEventType() == GameEventType.SUBSTITUTION_ON).findFirst().orElse(null);

            if (gameEvent == null) {
                return 0L;
            } else {
                GameEvent secondGameEvent = gamePlayer.getGame().getGameEvents().stream().filter(x -> x.getPlayer() == player && x.getGameEventType() == GameEventType.SUBSTITUTION_OF).findFirst().orElse(null);

                if (secondGameEvent == null) {
                    return 90 - gameEvent.getEventMinute();
                } else {
                    return secondGameEvent.getEventMinute() - gameEvent.getEventMinute();
                }
            }
        }
    }

    private Long numberOfMinutesPlayed(Player player, Set<GamePlayer> gamePlayers, List<GameEvent> gameEvents) {
        GamePlayer gamePlayer = gamePlayers.stream().filter(x -> x.getPlayer() == player).findFirst().orElse(null);

        if (gamePlayer != null) {
            if (gamePlayer.getGamePlayerStatus() == GamePlayerStatus.INJURED) {
                return 0L;
            } else if (gamePlayer.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD) {
                GameEvent gameEvent = gameEvents.stream().filter(x -> x.getPlayer() == player && x.getGameEventType() == GameEventType.SUBSTITUTION_OF).findFirst().orElse(null);

                if (gameEvent == null) {
                    return 90L;
                } else {
                    return gameEvent.getEventMinute();
                }
            } else {
                GameEvent gameEvent = gameEvents.stream().filter(x -> x.getPlayer() == player && x.getGameEventType() == GameEventType.SUBSTITUTION_ON).findFirst().orElse(null);

                if (gameEvent == null) {
                    return 0L;
                } else {
                    GameEvent secondGameEvent = gameEvents.stream().filter(x -> x.getPlayer() == player && x.getGameEventType() == GameEventType.SUBSTITUTION_OF).findFirst().orElse(null);

                    if (secondGameEvent == null) {
                        return 90 - gameEvent.getEventMinute();
                    } else {
                        return secondGameEvent.getEventMinute() - gameEvent.getEventMinute();
                    }
                }
            }
        } else {
            return 0L;
        }
    }

    public List<GetPlayerGamesResponse> getPlayerFinishedAndLiveGames(Long playerId) {
        if (this.playerRepository.findById(playerId).isPresent()) {
            Player mainPlayer = this.playerRepository.findById(playerId).get();

            List<GamePlayer> playerGames = this.gamePlayerRepository.findAllByPlayerAndGameGameStatus(mainPlayer, GameStatus.FINISHED);
            playerGames.addAll(this.gamePlayerRepository.findAllByPlayerAndGameGameStatus(mainPlayer, GameStatus.IN_PROGRESS));

            List<GetPlayerGamesResponse> response = new ArrayList<>();

            for (GamePlayer playerGame : playerGames) {

                Game game = playerGame.getGame();

                List<Long> squadTeamA = new ArrayList<>();
                List<Long> squadTeamB = new ArrayList<>();
                List<Long> substitutionsTeamA = new ArrayList<>();
                List<Long> substitutionsTeamB = new ArrayList<>();

                Set<GamePlayer> players = game.getPlayers();

                for (GamePlayer player : players) {
                    if (Objects.equals(player.getPlayer().getTeam().getId(), game.getTeamA().getId())) {
                        if (player.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD) {
                            squadTeamA.add(player.getPlayer().getId());
                        } else {
                            substitutionsTeamA.add(player.getPlayer().getId());
                        }
                    } else {
                        if (player.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD) {
                            squadTeamB.add(player.getPlayer().getId());
                        } else {
                            substitutionsTeamB.add(player.getPlayer().getId());
                        }
                    }
                }

                List<GameEvent> gameEvents = game.getGameEvents();
                List<GameEventsResponse> gameEventsResponses = new ArrayList<>();

                int numberOfGoals = 0;
                boolean isYellowCard = false;
                boolean isRedCard = false;
                Long numberOfMinutes = this.numberOfMinutesPlayed(mainPlayer, players, gameEvents);

                for (GameEvent gameEvent : gameEvents) {
                    if (gameEvent.getPlayer() == mainPlayer) {
                        if (gameEvent.getGameEventType() == GameEventType.GOAL) {
                            numberOfGoals++;
                        } else if (gameEvent.getGameEventType() == GameEventType.RED_CARD) {
                            isRedCard = true;
                        } else if (gameEvent.getGameEventType() == GameEventType.YELLOW_CARD) {
                            isYellowCard = true;
                        }
                    }

                    gameEventsResponses.add(new GameEventsResponse(gameEvent.getId(), gameEvent.getEventMinute(), gameEvent.getPlayer().getId(), gameEvent.getTeam().getId(), gameEvent.getGameEventType().toString()));
                }

                response.add(new GetPlayerGamesResponse(game.getId(), game.getTeamA().getId(), game.getTeamB().getId(), game.getScoreTeamA(), game.getScoreTeamB(), game.getGameStartDate(), game.getTeamA().getStadiumName(), gameEventsResponses, squadTeamA, squadTeamB, substitutionsTeamA, substitutionsTeamB, game.getRound(), numberOfMinutes, numberOfGoals, isYellowCard, isRedCard));
            }

            return response;
        } else {
            return new ArrayList<>();
        }
    }

    public SinglePlayerStatistics getSinglePlayerStatistics(Long playerId) {
        if (this.gamePlayerRepository.findAllByPlayerId(playerId).isPresent() && this.playerRepository.findById(playerId).isPresent()) {
            List<GamePlayer> games = this.gamePlayerRepository.findAllByPlayerId(playerId).get();
            Player player = this.playerRepository.findById(playerId).get();

            int goals = (int) player.getGameEvents().stream().filter((gameEvent -> gameEvent.getGameEventType() == GameEventType.GOAL)).count();
            int assists = (int) player.getGameEvents().stream().filter((gameEvent -> gameEvent.getGameEventType() == GameEventType.ASSIST)).count();
            int yellow = (int) player.getGameEvents().stream().filter((gameEvent -> gameEvent.getGameEventType() == GameEventType.YELLOW_CARD)).count();
            int red = (int) player.getGameEvents().stream().filter((gameEvent -> gameEvent.getGameEventType() == GameEventType.RED_CARD)).count();
            int firstSquadGames = games.size() - (int) player.getGameEvents().stream().filter((gameEvent -> gameEvent.getGameEventType() == GameEventType.SUBSTITUTION_ON)).count();
            int minutes = 0;
            int teamGoals = (int) player.getTeam().getGameEvents().stream().filter(gameEvent -> gameEvent.getGameEventType() == GameEventType.GOAL).count();
            int teamFinishedGames = this.gameService.countTeamFinishedGames(player.getTeam());

            for (GamePlayer gamePlayer : games) {
                minutes += this.numberOfMinutesPlayed(player, gamePlayer);
            }

            return new SinglePlayerStatistics(games.size(), firstSquadGames, minutes, goals, assists, yellow, red, teamFinishedGames, teamGoals);
        } else {
            return new SinglePlayerStatistics();
        }
    }
}
