package com.example.demo.league;

import com.example.demo.game.Game;
import com.example.demo.game.GameRepository;
import com.example.demo.game.GameStatus;
import com.example.demo.gameEvent.GameEvent;
import com.example.demo.gamePlayer.GamePlayer;
import com.example.demo.gamePlayer.GamePlayerStatus;
import com.example.demo.league.dto.AddLeagueRequest;
import com.example.demo.league.dto.GameEventsResponse;
import com.example.demo.league.dto.GetGamesResponse;
import com.example.demo.leagueTable.LeagueTable;
import com.example.demo.player.Player;
import com.example.demo.team.Team;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final GameRepository gameRepository;

    public League findById(Long id){
        if(this.leagueRepository.findById(id).isPresent()){
            return this.leagueRepository.findById(id).get();
        }else{
            return null;
        }
    }

    private boolean chefIfNotExist(Long leagueId){
        return this.leagueRepository.findById(leagueId).isEmpty();
    }

    public List<GetGamesResponse> getGames(List<Game> games){
        List<GetGamesResponse> result = new ArrayList<>();

        for(Game game : games){
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

            for(GameEvent gameEvent : gameEvents){
                gameEventsResponses.add(new GameEventsResponse(gameEvent.getEventMinute(), gameEvent.getPlayer().getId(), gameEvent.getGameEventType().toString()));
            }

            GetGamesResponse responseElement = new GetGamesResponse(game.getId(), game.getTeamA().getId(), game.getTeamB().getId(), game.getScoreTeamA(), game.getScoreTeamB(), game.getGameStartDate(), game.getTeamA().getStadiumName(), gameEventsResponses, squadTeamA, squadTeamB, substitutionsTeamA, substitutionsTeamB);

            result.add(responseElement);
        }

        return result;
    }

    public List<GetGamesResponse> getGamesByRound(Long leagueId, int round){
       if(this.chefIfNotExist(leagueId)){
           throw new IllegalStateException("League doesn't exist");
       }

      League league = this.leagueRepository.findById(leagueId).get();
      List<Game> games = this.gameRepository.findAllByRoundAndLeague(round, league);

      return this.getGames(games);
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

    public List<GetGamesResponse> getLiveGamesByRound(Long leagueId, int round) {
        if(this.leagueRepository.findById(leagueId).isPresent()){
            League league = this.leagueRepository.findById(leagueId).get();
            GameStatus gameStatus = GameStatus.IN_PROGRESS;

            List<Game> games = this.gameRepository.findAllByLeagueAndGameStatusAndRound(league, gameStatus, round);

            return this.getGames(games);
        }else{
            return new ArrayList<>();
        }
    }

    public List<GetGamesResponse> getFinishedGamesByRound(Long leagueId, int round){
        if(this.leagueRepository.findById(leagueId).isPresent()){
            League league = this.leagueRepository.findById(leagueId).get();
            GameStatus gameStatus = GameStatus.FINISHED;

            List<Game> games = this.gameRepository.findAllByLeagueAndGameStatusAndRound(league, gameStatus, round);

            return this.getGames(games);
        }else{
            return new ArrayList<>();
        }
    }

    public List<GetGamesResponse> getScheduledGamesByRound(Long leagueId, int round){
        if(this.leagueRepository.findById(leagueId).isPresent()){
            League league = this.leagueRepository.findById(leagueId).get();
            GameStatus gameStatus = GameStatus.SCHEDULED;

            List<Game> games = this.gameRepository.findAllByLeagueAndGameStatusAndRound(league, gameStatus, round);

            return this.getGames(games);
        }else{
            return new ArrayList<>();
        }
    }

    public Long addLeague(AddLeagueRequest request) {
        LeagueLevel leagueLevel = this.stringToLeagueLevel(request.getLeagueLevel());

        League newLeague =  new League(request.getLeagueName(), leagueLevel);

        this.leagueRepository.save(newLeague);

        return newLeague.getId();
    }

    private LeagueLevel stringToLeagueLevel(String leagueLevel) {
        if(leagueLevel.equalsIgnoreCase("Ekstraklasa")){
            return LeagueLevel.EKSTRAKLASA;
        }else if(leagueLevel.equalsIgnoreCase("ILiga")){
            return LeagueLevel.ILiga;
        }else if(leagueLevel.equalsIgnoreCase("IILiga")){
            return LeagueLevel.IILiga;
        }else if(leagueLevel.equalsIgnoreCase("IIILiga")){
            return LeagueLevel.IIILiga;
        }else if(leagueLevel.equalsIgnoreCase("IVLiga")){
            return LeagueLevel.IVLiga;
        }else if(leagueLevel.equalsIgnoreCase("KlasaOkregowa")){
            return LeagueLevel.KlasaOkregowa;
        }else if(leagueLevel.equalsIgnoreCase("KlasaA")){
            return LeagueLevel.KlasaA;
        }else{
            return LeagueLevel.KlasaB;
        }
    }
}
