package com.example.demo.player;

import com.example.demo.appUser.AppUser;
import com.example.demo.game.Game;
import com.example.demo.gameEvent.GameEvent;
import com.example.demo.team.Team;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="Player")
public class Player {

    @Id
    @SequenceGenerator(
            name = "player_sequence",
            sequenceName = "player_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "player_sequence"
    )
    private Long id;

    private String firstName;
    private String lastName;
    private String position;
    private int numberOfGoals;
    private int numberOfAssists;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "player")
    List<GameEvent> gameEvents;

    @ManyToMany(mappedBy = "players")
    private Set<Game> games;

    public Player(String firstName, String lastName, String position, int numberOfGoals, int numberOfAssists){
        this.firstName = firstName;
        this.lastName = lastName;
        this.numberOfGoals = numberOfGoals;
        this.numberOfAssists = numberOfAssists;
        this.position = position;
    }

    public Player(String firstName, String lastName, String position, int numberOfGoals, int numberOfAssists, Team team){
        this.firstName = firstName;
        this.lastName = lastName;
        this.numberOfGoals = numberOfGoals;
        this.numberOfAssists = numberOfAssists;
        this.team = team;
        this.position = position;
    }

    public void addGameEvent(GameEvent event){
        if(this.gameEvents == null){
            this.gameEvents = new ArrayList<>();
        }

        this.gameEvents.add(event);

        event.setPlayer(this);
    }

}
