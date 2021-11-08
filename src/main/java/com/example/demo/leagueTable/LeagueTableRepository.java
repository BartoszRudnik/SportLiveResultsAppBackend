package com.example.demo.leagueTable;

import com.example.demo.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeagueTableRepository extends JpaRepository<LeagueTable, Long> {
    Optional<LeagueTable> findByTeam(Team team);
}
