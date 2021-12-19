package com.example.demo.league;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    List<League> findAllByLeagueLevel(LeagueLevel leagueLevel);

}
