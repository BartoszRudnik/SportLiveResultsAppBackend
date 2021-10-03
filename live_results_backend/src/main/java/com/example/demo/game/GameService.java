package com.example.demo.game;

import com.example.demo.game.dto.AddGameRequest;
import com.example.demo.game.dto.UpdateScoreRequest;
import com.example.demo.gameEvent.GameEvent;
import com.example.demo.league.League;
import com.example.demo.league.LeagueService;
import com.example.demo.team.Team;
import com.example.demo.team.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final LeagueService leagueService;
    private final TeamService teamService;

    public Game getGame(Long gameId) {
        if (this.gameRepository.findById(gameId).isPresent()){
            return this.gameRepository.findById(gameId).get();
        }else{
            return null;
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


        Game game = new Game(0, 0, league, teamA, teamB, request.getGameStartDate(), gameStatus, request.getRound());

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
}
