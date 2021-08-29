package com.example.demo.league;

import com.example.demo.game.Game;
import com.example.demo.game.GameRepository;
import com.example.demo.leagueTable.LeagueTable;
import com.example.demo.player.Player;
import com.example.demo.team.Team;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final GameRepository gameRepository;

    private boolean chefIfNotExist(Long leagueId){
        return this.leagueRepository.findById(leagueId).isEmpty();
    }

    public List<Game> getGamesByRound(Long leagueId, int round){
       if(this.chefIfNotExist(leagueId)){
           throw new IllegalStateException("League doesn't exist");
       }

       League league = this.leagueRepository.findById(leagueId).get();

      return this.gameRepository.findAllByRoundAndLeague(round, league);
    }

    public List<LeagueTable> getLeagueTable(Long leagueId){
        if(this.chefIfNotExist(leagueId)){
            throw new IllegalStateException("League doesn't exist");
        }

        League league = this.leagueRepository.findById(leagueId).get();

        List<Team> leagueTeams = league.getTeams();
        List<LeagueTable> leagueTables = new ArrayList<>();

        for(Team team : leagueTeams){
            leagueTables.add(team.getLeagueTable());
        }

        Collections.sort(leagueTables);

        return leagueTables;
    }

    public List<Player> getLeagueBestScorers(Long leagueId){
        return this.getLeagueBest(leagueId, Comparator.comparingInt(Player::getNumberOfGoals));
    }

    public List<Player> getLeagueBestAssistants(Long leagueId){
        return this.getLeagueBest(leagueId, Comparator.comparingInt(Player::getNumberOfAssists));
    }

    public List<Player> getLeagueBestCanadianPoints(Long leagueId){
        return this.getLeagueBest(leagueId, Comparator.comparingInt(o -> o.getNumberOfGoals() + o.getNumberOfAssists()));
    }

    private List<Player> getLeagueBest(Long leagueId, Comparator<Player> playerComparator) {
        if(this.chefIfNotExist(leagueId)){
            throw new IllegalStateException("League doesn't exist");
        }

        League league = this.leagueRepository.findById(leagueId).get();

        List<Team> leagueTeams = league.getTeams();
        List<Player> leaguePlayers = new ArrayList<>();

        for(Team team : leagueTeams){
            leaguePlayers.addAll(team.getPlayers());
        }

        leaguePlayers.sort(playerComparator);

        return leaguePlayers.stream().limit(50).collect(Collectors.toList());
    }

}
