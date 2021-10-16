package com.example.demo.gamePlayer;

import com.example.demo.game.Game;
import com.example.demo.player.Player;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="GamePlayer")
public class GamePlayer {

    @Id
    @SequenceGenerator(
            name = "game_player_sequence",
            sequenceName = "game_player_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "game_player_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    private GamePlayerStatus gamePlayerStatus;

    public GamePlayer(Player player, Game game, GamePlayerStatus gamePlayerStatus){
        this.player = player;
        this.game = game;
        this.gamePlayerStatus = gamePlayerStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamePlayer that = (GamePlayer) o;
        return player.getId().equals(that.player.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(player.getId());
    }
}
