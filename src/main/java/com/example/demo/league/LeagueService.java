package com.example.demo.league;

import com.example.demo.game.Game;
import com.example.demo.game.GameRepository;
import com.example.demo.game.GameStatus;
import com.example.demo.gameEvent.GameEvent;
import com.example.demo.gameEvent.GameEventType;
import com.example.demo.gamePlayer.GamePlayer;
import com.example.demo.gamePlayer.GamePlayerStatus;
import com.example.demo.league.dto.*;
import com.example.demo.player.dto.GamePlayerResponse;
import com.example.demo.team.Team;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

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
            List<GamePlayerResponse> squadTeamA = new ArrayList<>();
            List<GamePlayerResponse> squadTeamB = new ArrayList<>();
            List<GamePlayerResponse> substitutionsTeamA = new ArrayList<>();
            List<GamePlayerResponse> substitutionsTeamB = new ArrayList<>();

            Set<GamePlayer> players = game.getPlayers();

            for(GamePlayer player : players){
                if(Objects.equals(player.getPlayer().getTeam().getId(), game.getTeamA().getId())){
                    if(player.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD){
                        squadTeamA.add(new GamePlayerResponse(player.getPlayer().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber()));
                    }else{
                        substitutionsTeamA.add(new GamePlayerResponse(player.getPlayer().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber()));
                    }
                }else{
                    if(player.getGamePlayerStatus() == GamePlayerStatus.FIRST_SQUAD) {
                        squadTeamB.add(new GamePlayerResponse(player.getPlayer().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber()));
                    }else{
                        substitutionsTeamB.add(new GamePlayerResponse(player.getPlayer().getId(), player.getGamePlayerPosition().toString(), player.getShirtNumber()));
                    }
                }
            }

            List<GameEvent> gameEvents = game.getGameEvents();
            List<GameEventsResponse> gameEventsResponses = new ArrayList<>();

            for(GameEvent gameEvent : gameEvents){
                gameEventsResponses.add(new GameEventsResponse(gameEvent.getId(), gameEvent.getEventMinute(), gameEvent.getPlayer().getId(), gameEvent.getTeam().getId(), gameEvent.getGameEventType().toString()));
            }

            String reporterMail = "";

            if(game.getReporter() != null){
                reporterMail = game.getReporter().getEmail();
            }

            GetGamesResponse responseElement = new GetGamesResponse(game.getId(), game.getTeamA().getId(), game.getTeamB().getId(), game.getScoreTeamA(), game.getScoreTeamB(), game.getGameStartDate(), game.getActualStartDate(), game.getTeamA().getStadiumName(), gameEventsResponses, squadTeamA, squadTeamB, substitutionsTeamA, substitutionsTeamB, game.getRound(), game.isBreak(), game.getPartOfGame(), game.getLengthOfPartOfGame(), reporterMail, game.getGameStatus().toString());

            result.add(responseElement);
        }

        return result;
    }

    public List<GetGamesResponse> getAllFinishedByLeague(Long leagueId) {
        if(this.chefIfNotExist(leagueId)){
            throw new IllegalStateException("League doesn't exist");
        }

        League league = this.leagueRepository.findById(leagueId).get();
        List<Game> games = this.gameRepository.findAllByLeagueAndGameStatus(league, GameStatus.FINISHED);

        return this.getGames(games);
    }

    public List<GetGamesResponse> getGamesByRound(Long leagueId, int round){
       if(this.chefIfNotExist(leagueId)){
           throw new IllegalStateException("League doesn't exist");
       }

      League league = this.leagueRepository.findById(leagueId).get();
      List<Game> games = this.gameRepository.findAllByRoundAndLeague(round, league);

      return this.getGames(games);
    }

    private int countScoredGoals(List<Game> games, Team team){
        int scoredGoals = 0;

        for(Game game : games){
           scoredGoals += game.getGameEvents().stream().filter((gameEvent -> gameEvent.getGameEventType() == GameEventType.GOAL && gameEvent.getTeam() == team)).count();
        }

        return scoredGoals;
    }

    private int countConcededGoals(List<Game> games, Team team){
        int scoredGoals = 0;

        for(Game game : games){
            scoredGoals += game.getGameEvents().stream().filter((gameEvent -> gameEvent.getGameEventType() == GameEventType.GOAL && gameEvent.getTeam() != team)).count();
        }

        return scoredGoals;
    }

    private int countPoints(List<Game> games, Team team){
        int points = 0;

        for(Game game : games){
            if(game.getTeamA() == team){
                if(game.getScoreTeamA() > game.getScoreTeamB()){
                    points += 3;
                }else if(game.getScoreTeamA() == game.getScoreTeamB()){
                    points += 1;
                }
            }else{
                if(game.getScoreTeamA() < game.getScoreTeamB()){
                    points += 3;
                }else if(game.getScoreTeamA() == game.getScoreTeamB()){
                    points += 1;
                }
            }
        }

        return points;
    }

    public List<GetLeagueTableResponse> getFormTable(Long leagueId, int size) {
        if(this.chefIfNotExist(leagueId)){
            throw new IllegalStateException("League doesn't exist");
        }
        League league = this.leagueRepository.findById(leagueId).get();

        List<Team> leagueTeams = league.getTeams();
        List<GetLeagueTableResponse> leagueTables = new ArrayList<>();

        for(Team team : leagueTeams){
            List<Game> allGames = gameRepository.findTeamFinishedGames(team, GameStatus.FINISHED);
            allGames.sort(Comparator.comparing(Game::getGameStartDate).reversed());

            List<Game> games;

            if(size > allGames.size()){
                games = allGames;
            }else{
                games = allGames.subList(0, size);
            }

            leagueTables.add(new GetLeagueTableResponse(team.getTeamName(), team.getId(), games.size(), this.countScoredGoals(games, team), this.countConcededGoals(games, team), this.countPoints(games, team)));
        }

        return leagueTables;
    }


    public List<GetLeagueTableResponse> getHomeTable(Long leagueId) {
        if(this.chefIfNotExist(leagueId)){
            throw new IllegalStateException("League doesn't exist");
        }
        League league = this.leagueRepository.findById(leagueId).get();

        List<Team> leagueTeams = league.getTeams();
        List<GetLeagueTableResponse> leagueTables = new ArrayList<>();

        for(Team team : leagueTeams){
            List<Game> games = gameRepository.findTeamHomeFinishedGames(team, GameStatus.FINISHED);

            leagueTables.add(new GetLeagueTableResponse(team.getTeamName(), team.getId(), games.size(), this.countScoredGoals(games, team), this.countConcededGoals(games, team), this.countPoints(games, team)));
        }
        Collections.sort(leagueTables);

        return leagueTables;
    }

    public List<GetLeagueTableResponse> getAwayTable(Long leagueId) {
        if(this.chefIfNotExist(leagueId)){
            throw new IllegalStateException("League doesn't exist");
        }
        League league = this.leagueRepository.findById(leagueId).get();

        List<Team> leagueTeams = league.getTeams();
        List<GetLeagueTableResponse> leagueTables = new ArrayList<>();

        for(Team team : leagueTeams){
            List<Game> games = gameRepository.findTeamAwayFinishedGames(team, GameStatus.FINISHED);

            leagueTables.add(new GetLeagueTableResponse(team.getTeamName(), team.getId(), games.size(), this.countScoredGoals(games, team), this.countConcededGoals(games, team), this.countPoints(games, team)));
        }
        Collections.sort(leagueTables);

        return leagueTables;
    }

    public List<GetLeagueTableResponse> getLeagueTable(Long leagueId){
        if(this.chefIfNotExist(leagueId)){
            throw new IllegalStateException("League doesn't exist");
        }

        League league = this.leagueRepository.findById(leagueId).get();

        List<Team> leagueTeams = league.getTeams();
        List<GetLeagueTableResponse> leagueTables = new ArrayList<>();

        for(Team team : leagueTeams){
            leagueTables.add(new GetLeagueTableResponse(team.getTeamName(), team.getId(), team.getLeagueTable().getGames(), team.getLeagueTable().getGoalsScored(), team.getLeagueTable().getGoalsConceded(), team.getLeagueTable().getPoints()));
        }
        Collections.sort(leagueTables);

        return leagueTables;
    }

    public List<Long> getLiveGamesByRound(Long leagueId, int round) {
        if(this.leagueRepository.findById(leagueId).isPresent()){
            League league = this.leagueRepository.findById(leagueId).get();
            GameStatus gameStatus = GameStatus.IN_PROGRESS;

            List<Game> games = this.gameRepository.findAllByLeagueAndGameStatusAndRound(league, gameStatus, round);
            List<Long> gameIds = new ArrayList<>();

            for(Game game : games){
                gameIds.add(game.getId());
            }

            return gameIds;
        }else{
            return new ArrayList<>();
        }
    }

    public List<Long> getFinishedGamesByRound(Long leagueId, int round){
        if(this.leagueRepository.findById(leagueId).isPresent()){
            League league = this.leagueRepository.findById(leagueId).get();
            GameStatus gameStatus = GameStatus.FINISHED;

            List<Game> games = this.gameRepository.findAllByLeagueAndGameStatusAndRound(league, gameStatus, round);
            List<Long> gameIds = new ArrayList<>();

            for(Game game : games){
                gameIds.add(game.getId());
            }

            return gameIds;
        }else{
            return new ArrayList<>();
        }
    }

    public List<Long> getScheduledGamesByRound(Long leagueId, int round){
        if(this.leagueRepository.findById(leagueId).isPresent()){
            League league = this.leagueRepository.findById(leagueId).get();
            GameStatus gameStatus = GameStatus.SCHEDULED;

            List<Game> games = this.gameRepository.findAllByLeagueAndGameStatusAndRound(league, gameStatus, round);
            List<Long> gameIds = new ArrayList<>();

            for(Game game : games){
                gameIds.add(game.getId());
            }

            return gameIds;
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

    public GetLeaguesResponse findSingleLeague(Long leagueId){
        if(this.leagueRepository.findById(leagueId).isPresent()){
            League league = this.leagueRepository.findById(leagueId).get();

            return new GetLeaguesResponse(leagueId, league.getLeagueName(), league.getLeagueLevel().toString());
        }else{
            return new GetLeaguesResponse();
        }
    }

    public List<GetLeaguesResponse> findAllByLeagueLevel(String leagueLevelName) {
        LeagueLevel leagueLevel = this.stringToLeagueLevel(leagueLevelName);

        List<League> leagues = this.leagueRepository.findAllByLeagueLevel(leagueLevel);
        List<GetLeaguesResponse> resultList = new ArrayList<>();

        for(League league : leagues){
            resultList.add(new GetLeaguesResponse(league.getId(), league.getLeagueName(), league.getLeagueLevel().toString()));
        }

        return resultList;
    }


}
