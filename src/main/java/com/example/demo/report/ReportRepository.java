package com.example.demo.report;

import com.example.demo.appUser.AppUser;
import com.example.demo.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByAppUser(AppUser user);
    List<Report> findAllByGame(Game game);
}
