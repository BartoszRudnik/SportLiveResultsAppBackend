package com.example.demo.player;

import com.example.demo.league.League;
import com.example.demo.league.LeagueRepository;
import com.example.demo.player.dto.AddPlayerRequest;
import com.example.demo.player.dto.SinglePlayerResponse;
import com.example.demo.player.dto.UpdatePlayerDataRequest;
import com.example.demo.team.Team;
import com.example.demo.team.TeamRepository;
import com.example.demo.team.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamService teamService;
    private final TeamRepository teamRepository;
    private final LeagueRepository leagueRepository;

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
}
