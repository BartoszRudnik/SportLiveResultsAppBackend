package com.example.demo.gameStatistics;

import com.example.demo.game.Game;
import com.example.demo.game.GameRepository;
import com.example.demo.gameStatistics.dto.GetGameStatistics;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class GameStatisticsService {
    private final GameRepository gameRepository;
    private final GameStatisticsRepository gameStatisticsRepository;

    public GetGameStatistics getGameStatistics(Long gameId){
        if(this.gameRepository.findById(gameId).isPresent()){
            GameStatistics stats = this.gameRepository.findById(gameId).get().getGameStatistics();

            return new GetGameStatistics(stats.getFoulsTeamA(), stats.getFoulsTeamB(), stats.getCornersTeamA(), stats.getCornersTeamB(), stats.getOffsidesTeamA(), stats.getOffsidesTeamB(), stats.getShootsTeamA(), stats.getShootsTeamB());
        }else{
            return new GetGameStatistics();
        }
    }

    public void addFoul(Long teamId, Long gameId){
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();
            GameStatistics gameStatistics = game.getGameStatistics();

            if(Objects.equals(game.getTeamA().getId(), teamId)){
                gameStatistics.setFoulsTeamA(gameStatistics.getFoulsTeamA() + 1);
            }else{
                gameStatistics.setFoulsTeamB(gameStatistics.getFoulsTeamB() + 1);
            }

            this.gameStatisticsRepository.save(gameStatistics);
        }
    }

    public void addCorner(Long teamId, Long gameId){
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();
            GameStatistics gameStatistics = game.getGameStatistics();

            if(Objects.equals(game.getTeamA().getId(), teamId)){
                gameStatistics.setCornersTeamA(gameStatistics.getCornersTeamA() + 1);
            }else{
                gameStatistics.setCornersTeamB(gameStatistics.getCornersTeamB() + 1);
            }

            this.gameStatisticsRepository.save(gameStatistics);
        }
    }

    public void addShoot(Long teamId, Long gameId){
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();
            GameStatistics gameStatistics = game.getGameStatistics();

            if(Objects.equals(game.getTeamA().getId(), teamId)){
                gameStatistics.setShootsTeamA(gameStatistics.getShootsTeamA() + 1);
            }else{
                gameStatistics.setShootsTeamB(gameStatistics.getShootsTeamB() + 1);
            }

            this.gameStatisticsRepository.save(gameStatistics);
        }
    }

    public void addOffside(Long teamId, Long gameId){
        if(this.gameRepository.findById(gameId).isPresent()){
            Game game = this.gameRepository.findById(gameId).get();
            GameStatistics gameStatistics = game.getGameStatistics();

            if(Objects.equals(game.getTeamA().getId(), teamId)){
                gameStatistics.setOffsidesTeamA(gameStatistics.getOffsidesTeamA() + 1);
            }else{
                gameStatistics.setOffsidesTeamB(gameStatistics.getOffsidesTeamB() + 1);
            }

            this.gameStatisticsRepository.save(gameStatistics);
        }
    }
}
