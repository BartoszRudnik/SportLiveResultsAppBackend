package com.example.demo.gameStatistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameStatisticsRepository extends JpaRepository<GameStatistics, Long> {

}
