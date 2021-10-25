package com.example.demo.team;

import com.example.demo.game.Game;
import com.example.demo.league.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findAllByLeague(League league);

}
