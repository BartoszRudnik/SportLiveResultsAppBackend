package com.example.demo.gameEvent;

import com.example.demo.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameEventRepository extends JpaRepository<GameEvent, Long> {

    List<GameEvent> findAllByGame(Game game);

}
