package com.example.demo.gamePlayer;

import com.example.demo.game.GameStatus;
import com.example.demo.player.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
    List<GamePlayer> findAllByPlayer(Player player);
    List<GamePlayer> findAllByPlayerAndGameGameStatus(Player player, GameStatus gameStatus);
}
