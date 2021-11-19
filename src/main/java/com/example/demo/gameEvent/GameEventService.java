package com.example.demo.gameEvent;

import com.example.demo.game.Game;
import com.example.demo.game.GameRepository;
import com.example.demo.game.GameService;
import com.example.demo.game.GameStatus;
import com.example.demo.gameEvent.dto.FullEventRequest;
import com.example.demo.gameEvent.dto.IncompleteEventRequest;
import com.example.demo.gameEvent.dto.UpdateEventRequest;
import com.example.demo.gamePlayer.GamePlayer;
import com.example.demo.gamePlayer.GamePlayerStatus;
import com.example.demo.leagueTable.LeagueTable;
import com.example.demo.leagueTable.LeagueTableRepository;
import com.example.demo.player.Player;
import com.example.demo.player.PlayerService;
import com.example.demo.team.Team;
import com.example.demo.team.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class GameEventService {
    private final LeagueTableRepository leagueTableRepository;
    private final GameRepository gameRepository;
    private final GameEventRepository gameEventRepository;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final GameService gameService;

    public List<GameEvent> findAllByGame(Long gameId){
        Game game = this.gameService.getGame(gameId);

        if(game != null){
            return this.gameEventRepository.findAllByGame(game);
        }else{
            return null;
        }
    }

    @Transactional
    public void deleteGameEvent(Long gameEventId){
        if(this.gameEventRepository.findById(gameEventId).isPresent()){
            GameEvent gameEvent = this.gameEventRepository.findById(gameEventId).get();

            if(gameEvent.getGameEventType() == GameEventType.GOAL){
               if(gameEvent.getTeam() == gameEvent.getGame().getTeamA()){
                   gameEvent.getGame().setScoreTeamA(gameEvent.getGame().getScoreTeamA() - 1);
               }else{
                   gameEvent.getGame().setScoreTeamB(gameEvent.getGame().getScoreTeamB() - 1);
               }

               this.gameRepository.save(gameEvent.getGame());
            }

            this.gameEventRepository.deleteById(gameEventId);
        }
    }

    @Transactional
    public void deleteById(Long id){
        this.gameEventRepository.deleteById(id);
    }

    public void updateEvent(UpdateEventRequest request){
        if(this.gameEventRepository.findById(request.getEventId()).isEmpty()){
            throw new IllegalStateException("Event doesn't exist");
        }

        GameEvent event = this.gameEventRepository.findById(request.getEventId()).get();

        Game game = this.gameService.getGame(request.getGameId());
        Player player = this.playerService.getPlayer(request.getPlayerId());
        Team team = this.teamService.getTeam(request.getTeamId());

        if(event.getGameEventType() == GameEventType.GOAL){
            if(event.getTeam() == game.getTeamA()){
                game.setScoreTeamA(game.getScoreTeamA() - 1);
            }else{
                game.setScoreTeamB(game.getScoreTeamB() - 1);
            }
        }

        event.setGameEventType(this.gameEventTypeFromString(request.getEventType()));
        event.setPlayer(player);
        event.setTeam(team);

        if(event.getGameEventType() == GameEventType.GOAL){
            if(event.getTeam() == game.getTeamA()){
                game.setScoreTeamA(game.getScoreTeamA() + 1);
            }else{
                game.setScoreTeamB(game.getScoreTeamB() + 1);
            }
        }

        this.gameRepository.save(game);
        this.gameEventRepository.save(event);
    }

    public Long addIncompleteGameEvent(IncompleteEventRequest request){
        GameEventType eventType = this.gameEventTypeFromString(request.getEventType());

        Team team = this.teamService.getTeam(request.getTeamId());
        Game game = this.gameService.getGame(request.getGameId());

        GameEvent newGameEvent = new GameEvent(Duration.between(LocalDateTime.now(), game.getActualStartDate()).toMinutes() + (long) game.getLengthOfPartOfGame() * game.getPartOfGame() + 1, eventType, team, game);

        team.addGameEvent(newGameEvent);
        game.addGameEvent(newGameEvent);

        this.gameEventRepository.save(newGameEvent);

        return newGameEvent.getId();
    }

    public Long addFullGameEvent(FullEventRequest request){
        GameEventType eventType = this.gameEventTypeFromString(request.getEventType());

        Team team = this.teamService.getTeam(request.getTeamId());
        Game game = this.gameService.getGame(request.getGameId());
        Player player = this.playerService.getPlayer(request.getPlayerId());

        if(eventType == GameEventType.GOAL){
            if(game.getTeamA() == team){
                game.setScoreTeamA(game.getScoreTeamA() + 1);
            }else{
                game.setScoreTeamB(game.getScoreTeamB() + 1);
            }
        }

        GameEvent newGameEvent = new GameEvent(Duration.between(game.getActualStartDate(), LocalDateTime.now()).toMinutes() + (long) game.getLengthOfPartOfGame() * game.getPartOfGame() + 1, eventType, team, game, player);

        team.addGameEvent(newGameEvent);
        game.addGameEvent(newGameEvent);
        player.addGameEvent(newGameEvent);

        this.gameEventRepository.save(newGameEvent);

        return newGameEvent.getId();
    }

    private GameEventType gameEventTypeFromString(String eventType){
        if(eventType.equalsIgnoreCase("goal")){
            return GameEventType.GOAL;
        }else if(eventType.equalsIgnoreCase("substitution_on")){
            return GameEventType.SUBSTITUTION_ON;
        }else if(eventType.equalsIgnoreCase("substitution_of")){
            return GameEventType.SUBSTITUTION_OF;
        }
        else if(eventType.equalsIgnoreCase("yellow_card")){
            return GameEventType.YELLOW_CARD;
        }else{
            return GameEventType.RED_CARD;
        }
    }

    public void gameStart(Long gameId) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();

            game.setGameStatus(GameStatus.IN_PROGRESS);
            game.setPartOfGame(0);
            game.setActualStartDate(LocalDateTime.now());

            this.gameRepository.save(game);
        }
    }

    public void halfStart(Long gameId) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();

            game.setBreak(false);
            game.setPartOfGame(1);
            game.setActualStartDate(LocalDateTime.now());

            this.gameRepository.save(game);
        }
    }

    public void halfEnd(Long gameId) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();

            game.setBreak(true);

            this.gameRepository.save(game);
        }
    }

    public void gameEnd(Long gameId) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();

            game.setGameStatus(GameStatus.FINISHED);

            this.gameRepository.save(game);

            this.updateLeagueTable(game.getTeamA());
            this.updateLeagueTable(game.getTeamB());
        }
    }

    private void updateLeagueTable(Team team){
        if(this.leagueTableRepository.findByTeam(team).isPresent()){
            LeagueTable table = this.leagueTableRepository.findByTeam(team).get();
            List<Game> teamAFinishedGames = this.gameRepository.findTeamFinishedGames(team, GameStatus.FINISHED);

            System.out.println(teamAFinishedGames.size() + " dupaa");

            int teamGoalsScored = 0;
            int teamGoalsConceded = 0;
            int teamPoints = 0;
            int teamGames = 0;

            for(Game game : teamAFinishedGames){
               teamGames++;

               if(game.getTeamA() == team) {
                   teamGoalsScored += game.getScoreTeamA();
                   teamGoalsConceded += game.getScoreTeamB();
                   if (game.getScoreTeamA() > game.getScoreTeamB()) {
                       teamPoints += 3;
                   } else if (game.getScoreTeamA() == game.getScoreTeamB()) {
                       teamPoints += 1;
                   }
               }else{
                   teamGoalsScored += game.getScoreTeamB();
                   teamGoalsConceded += game.getScoreTeamA();
                   if(game.getScoreTeamB() > game.getScoreTeamA()){
                       teamPoints += 3;
                   }else if(game.getScoreTeamA() == game.getScoreTeamB()){
                       teamPoints += 1;
                   }
               }
            }

            table.setPoints(teamPoints);
            table.setGames(teamGames);
            table.setGoalsConceded(teamGoalsConceded);
            table.setGoalsScored(teamGoalsScored);

            this.leagueTableRepository.save(table);
        }
    }

    public List<Long> substitution(Long playerOffId, Long playerOnId, Long gameId) {
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();

            Set<GamePlayer> players = game.getPlayers();

            Optional<GamePlayer> playerOff = players.stream().filter(gamePlayer -> Objects.equals(gamePlayer.getPlayer().getId(), playerOffId)).findFirst();
            Optional<GamePlayer> playerOn = players.stream().filter(gamePlayer -> Objects.equals(gamePlayer.getPlayer().getId(), playerOnId)).findFirst();

            if(playerOff.isPresent() && playerOn.isPresent()){
                GamePlayer gamePlayerOff = playerOff.get();
                GamePlayer gamePlayerOn = playerOn.get();

                gamePlayerOff.setGamePlayerStatus(GamePlayerStatus.SUBSTITUTION);
                gamePlayerOn.setGamePlayerStatus(GamePlayerStatus.FIRST_SQUAD);

                this.gameRepository.save(game);

                GameEvent eventOff = new GameEvent(Duration.between(game.getActualStartDate(), LocalDateTime.now()).toMinutes() + (long) game.getLengthOfPartOfGame() * game.getPartOfGame() + 1, GameEventType.SUBSTITUTION_OF, gamePlayerOff.getPlayer().getTeam(), game, gamePlayerOff.getPlayer());
                GameEvent eventOn = new GameEvent(Duration.between(game.getActualStartDate(), LocalDateTime.now()).toMinutes() + (long) game.getLengthOfPartOfGame() * game.getPartOfGame() + 1, GameEventType.SUBSTITUTION_ON, gamePlayerOn.getPlayer().getTeam(), game, gamePlayerOn.getPlayer());

                game.addGameEvent(eventOff);
                game.addGameEvent(eventOn);

                gamePlayerOff.getPlayer().getTeam().addGameEvent(eventOff);
                gamePlayerOn.getPlayer().getTeam().addGameEvent(eventOn);

                gamePlayerOff.getPlayer().addGameEvent(eventOff);
                gamePlayerOn.getPlayer().addGameEvent(eventOn);

                this.gameEventRepository.save(eventOff);
                this.gameEventRepository.save(eventOn);

                List<Long> result = new ArrayList<>();

                result.add(eventOff.getId());
                result.add(eventOn.getId());

                return result;
            }
        }
        return  new ArrayList<>();
    }
}
