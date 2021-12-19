package com.example.demo.game;

import com.example.demo.league.League;
import com.example.demo.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findAllByRoundAndLeague(int round, League league);

    @Query("select g from Game g where (g.teamA = ?1 OR g.teamB = ?1) AND g.gameStatus = ?2")
    List<Game> findTeamFinishedGames(Team team, GameStatus gameStatus);

    @Query("select g from Game g" +
            " where (g.teamA = ?1 OR g.teamB = ?1) AND g.gameStatus = ?2")
    List<Game> findTeamScheduledGames(Team team, GameStatus gameStatus);

    @Query("select g from Game g" +
            " where (g.teamA = ?1 OR g.teamB = ?1) AND g.gameStatus = ?2")
    List<Game> findTeamLiveGames(Team team, GameStatus gameStatus);

    List<Game> findAllByLeagueAndGameStatusAndTeamAOrTeamB(League league, GameStatus gameStatus, Team teamA, Team teamB);
    List<Game> findAllByLeagueAndTeamAOrTeamB(League league, Team teamA, Team teamB);
    List<Game> findAllByLeagueAndGameStatusAndRound(League league, GameStatus gameStatus, int round);
    List<Game> findAllByLeagueAndGameStatus(League league, GameStatus gameStatus);
}
