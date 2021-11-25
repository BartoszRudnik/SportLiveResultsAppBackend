package com.example.demo.gameEvent;

import com.example.demo.game.Game;
import com.example.demo.player.Player;
import com.example.demo.team.Team;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="GameEvent")
public class GameEvent {

    @Id
    @SequenceGenerator(
            name = "game_event_sequence",
            sequenceName = "game_event_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "game_event_sequence"
    )
    private Long id;

    private Long eventMinute;
    private String message;

    @Enumerated(EnumType.STRING)
    private GameEventType gameEventType;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    public GameEvent(Long eventMinute, GameEventType gameEventType, Team team, Game game, String message){
        this.eventMinute = eventMinute;
        this.gameEventType = gameEventType;
        this.team = team;
        this.game = game;
        this.message = message;
    }

    public GameEvent(Long eventMinute, GameEventType gameEventType, Game game, String message){
        this.eventMinute = eventMinute;
        this.gameEventType = gameEventType;
        this.game = game;
        this.message = message;
    }

    public GameEvent(Long eventMinute, GameEventType gameEventType, Team team, Game game, Player player, String message){
        this.eventMinute = eventMinute;
        this.gameEventType = gameEventType;
        this.team = team;
        this.game = game;
        this.player = player;
        this.message = message;
    }

}
