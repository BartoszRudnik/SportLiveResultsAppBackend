package com.example.demo.gamePlayer;

import com.example.demo.game.Game;
import com.example.demo.game.GameStatus;
import com.example.demo.player.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
    Optional<List<GamePlayer>> findAllByPlayerId(Long playerId);
    List<GamePlayer> findAllByPlayer(Player player);
    List<GamePlayer> findAllByPlayerAndGameGameStatus(Player player, GameStatus gameStatus);
    @Transactional
    void deleteByGameAndPlayer(Game game, Player player);
}
