package com.example.demo.game;

import com.example.demo.gameEvent.GameEvent;
import com.example.demo.league.League;
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
    private Long id;

    private int scoreTeamA;
    private int scoreTeamB;

    @ManyToOne
    @JoinColumn(name = "teamA_id")
    private Team teamA;

    @ManyToOne
    @JoinColumn(name = "teamB_id")
    private Team teamB;

    @ManyToOne
    @JoinColumn(name = "league_id")
    private League league;

    private Date gameStartDate;
    private GameStatus gameStatus;
    private int round;

    @OneToMany(mappedBy = "game")
    private List<GameEvent> gameEvents;

    public Game(int scoreTeamA, int scoreTeamB, League league, Team teamA, Team teamB, Date gameStartDate, GameStatus gameStatus, int round){
        this.scoreTeamA = scoreTeamA;
        this.scoreTeamB = scoreTeamB;
        this.league = league;
        this.teamA = teamA;
        this.teamB = teamB;
        this.gameStartDate = gameStartDate;
        this.gameStatus = gameStatus;
        this.round = round;
    }

    public Game(int scoreTeamA, int scoreTeamB, League league, Team teamA, Team teamB, Date gameStartDate, GameStatus gameStatus, List<GameEvent> gameEvents, int round){
        this.scoreTeamA = scoreTeamA;
        this.scoreTeamB = scoreTeamB;
        this.league = league;
        this.teamA = teamA;
        this.teamB = teamB;
        this.gameStartDate = gameStartDate;
        this.gameStatus = gameStatus;
        this.gameEvents = gameEvents;
        this.round = round;
    }

    public void addGameEvent(GameEvent event){
        if(this.gameEvents == null){
            this.gameEvents = new ArrayList<>();
        }

        this.gameEvents.add(event);
    }

}
