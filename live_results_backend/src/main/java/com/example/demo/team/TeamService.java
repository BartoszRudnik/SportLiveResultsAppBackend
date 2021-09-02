package com.example.demo.team;

import com.example.demo.game.Game;
import com.example.demo.game.GameRepository;
import com.example.demo.game.GameStatus;
import com.example.demo.player.Player;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;

    public Team getTeam(Long teamId){
        if(this.teamRepository.findById(teamId).isPresent()){
            return this.teamRepository.findById(teamId).get();
        }else{
            return null;
        }
    }

    public List<Player> getTeamPlayers(Long teamId){
        if(this.checkIfTeamNotExist(teamId)){
            throw new IllegalStateException("Team doesn't exist");
        }

        return this.teamRepository.findById(teamId).get().getPlayers();
    }

    public List<Game> getAllTeamGames(Long teamId){
        if(this.checkIfTeamNotExist(teamId)){
            throw new IllegalStateException("Team doesn't exist");
        }

        Team team = this.teamRepository.findById(teamId).get();

        return this.gameRepository.findAllByLeagueAndTeamAOrTeamB(team.getLeague(), team, team);
    }

    public List<Game> getAllTeamFinishedGames(Long teamId){
        if(this.checkIfTeamNotExist(teamId)){
            throw new IllegalStateException("Team doesn't exist");
        }

        Team team = this.teamRepository.findById(teamId).get();

        return this.gameRepository.findAllByLeagueAndGameStatusAndTeamAOrTeamB(team.getLeague(), GameStatus.FINISHED, team, team);
    }

    public List<Game> getAllTeamScheduledGame(Long teamId){
        if(this.checkIfTeamNotExist(teamId)){
            throw new IllegalStateException("Team doesn't exist");
        }

        Team team = this.teamRepository.findById(teamId).get();

        return this.gameRepository.findAllByLeagueAndGameStatusAndTeamAOrTeamB(team.getLeague(), GameStatus.SCHEDULED, team, team);
    }

    private boolean checkIfTeamNotExist(Long teamId){
        return this.teamRepository.findById(teamId).isEmpty();
    }

}
