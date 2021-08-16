package com.example.demo.game;

import com.example.demo.gameEvent.GameEvent;
import com.example.demo.team.Team;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="Game")
public class Game {

    @Id
    @SequenceGenerator(
            name = "game_sequence",
            sequenceName = "game_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "game_sequence"
    )
    private Long gameId;

    private int scoreTeamA;
    private int scoreTeamB;

    @ManyToOne
    private Team teamA;

    @ManyToOne
    private Team teamB;

    private League league;

    private Date gameStartDate;
    private String gameStatus;

    @OneToMany(mappedBy = "game")
    List<GameEvent> gameEvents;

    public void addGameEvent(GameEvent event){
        if(this.gameEvents == null){
            this.gameEvents = new ArrayList<>();
        }

        this.gameEvents.add(event);
    }

}
