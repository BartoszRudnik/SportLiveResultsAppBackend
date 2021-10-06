package com.example.demo.leagueTable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueTableRepository extends JpaRepository<LeagueTable, Long> {
}
