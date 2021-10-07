package com.example.demo.team;

import com.example.demo.game.Game;
import com.example.demo.game.GameRepository;
import com.example.demo.game.GameStatus;
import com.example.demo.league.League;
import com.example.demo.league.LeagueRepository;
import com.example.demo.leagueTable.LeagueTable;
import com.example.demo.leagueTable.LeagueTableRepository;
import com.example.demo.player.Player;
import com.example.demo.player.PlayerRepository;
import com.example.demo.team.dto.AddTeamRequest;
import com.example.demo.team.dto.SingleTeamResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final LeagueRepository leagueRepository;
    private final LeagueTableRepository leagueTableRepository;

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

    public Long addTeam(AddTeamRequest request) {
        List<Player> teamPlayers = new ArrayList<>();
        League league = null;

        for(Long playerId : request.getPlayers()){
            if(this.playerRepository.findById(playerId).isPresent()){
                Player newPlayer = this.playerRepository.findById(playerId).get();

                teamPlayers.add(newPlayer);
            }
        }

        if(this.leagueRepository.findById(request.getLeagueId()).isPresent()){
            league = this.leagueRepository.findById(request.getLeagueId()).get();
        }

        Team newTeam = new Team(request.getTeamName(), request.getCity(), request.getStadiumName(), teamPlayers, league);

        this.teamRepository.save(newTeam);

        LeagueTable newLeagueTable = new LeagueTable(newTeam);
        newTeam.setLeagueTable(newLeagueTable);

        this.leagueTableRepository.save(newLeagueTable);
        this.teamRepository.save(newTeam);

        return newTeam.getId();
    }

    public List<SingleTeamResponse> getTeamsFromLeague(Long leagueId) {
        if(this.leagueRepository.findById(leagueId).isPresent()) {
            League league = this.leagueRepository.findById(leagueId).get();

            List<Team> teams = this.teamRepository.findAllByLeague(league);

            List<SingleTeamResponse> resultList = new ArrayList<>();

            for(Team team : teams){
                resultList.add(new SingleTeamResponse(team.getId(), team.getTeamName(), team.getCity(), team.getStadiumName()));
            }

            return resultList;
        }else{
            return new ArrayList<>();
        }
    }
}
