package com.example.demo.player;

import com.example.demo.player.dto.AddPlayerRequest;
import com.example.demo.team.Team;
import com.example.demo.team.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamService teamService;

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

}
