package com.example.demo.game;

import com.example.demo.gamePlayer.GamePlayer;
import com.example.demo.league.League;
import com.example.demo.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findAllByRoundAndLeague(int round, League league);

    List<Game> findAllByLeagueAndGameStatusAndTeamAOrTeamB(League league, GameStatus gameStatus, Team teamA, Team teamB);
    List<Game> findAllByLeagueAndTeamAOrTeamB(League league, Team teamA, Team teamB);
    List<Game> findAllByLeagueAndGameStatusAndRound(League league, GameStatus gameStatus, int round);
    List<Game> findAllByLeagueAndGameStatus(League league, GameStatus gameStatus);
}
