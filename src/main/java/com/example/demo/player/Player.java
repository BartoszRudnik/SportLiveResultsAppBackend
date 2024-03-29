package com.example.demo.player;

import com.example.demo.appUser.AppUser;
import com.example.demo.game.Game;
import com.example.demo.gameEvent.GameEvent;
import com.example.demo.gamePlayer.GamePlayer;
import com.example.demo.team.Team;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
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

    @Column(nullable = false)
    private String firstName;
    private String lastName;
    private String position;
    private int numberOfAssists;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "player")
    private List<GameEvent> gameEvents;

    @OneToMany(mappedBy = "player")
    private List<GamePlayer> games;

    public Player(String firstName, String lastName, String position, int numberOfAssists){
        this.firstName = firstName;
        this.lastName = lastName;
        this.numberOfAssists = numberOfAssists;
        this.position = position;
    }

    public Player(String firstName, String lastName, String position, int numberOfAssists, Team team){
        this.firstName = firstName;
        this.lastName = lastName;
        this.numberOfAssists = numberOfAssists;
        this.team = team;
        this.position = position;
    }

    public void addGame(GamePlayer game){
        if(this.games == null){
            this.games = new ArrayList<>();
        }

        this.games.add(game);

        game.setPlayer(this);
    }

    public void removeGame(GamePlayer game){
        if(this.games != null){
            this.games.remove(game);
        }
    }

    public void addGameEvent(GameEvent event){
        if(this.gameEvents == null){
            this.gameEvents = new ArrayList<>();
        }

        this.gameEvents.add(event);

        event.setPlayer(this);
    }
}
