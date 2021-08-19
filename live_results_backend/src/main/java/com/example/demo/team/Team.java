package com.example.demo.team;

import com.example.demo.gameEvent.GameEvent;
import com.example.demo.league.League;
import com.example.demo.leagueTable.LeagueTable;
import com.example.demo.player.Player;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="Team")
public class Team {

    @Id
    @SequenceGenerator(
            name = "team_sequence",
            sequenceName = "team_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "team_sequence"
    )
    private Long id;

    private String teamName;
    private String city;
    private String stadiumName;

    @OneToMany(mappedBy = "team")
    private List<Player> players;

    @OneToMany(mappedBy = "team")
    private List<GameEvent> gameEvents;

    @ManyToOne
    @JoinColumn(name = "league_id")
    private League league;

    @ManyToOne
    @JoinColumn(name = "league_table_id")
    private LeagueTable leagueTable;

    public Team(String teamName, String city, String stadiumName, List<Player> players, League league, LeagueTable leagueTable){
        this.teamName = teamName;
        this.city = city;
        this.stadiumName = stadiumName;
        this.players = players;
        this.league = league;
        this.leagueTable = leagueTable;
    }

    public void addGameEvent(GameEvent event){
        if(this.gameEvents == null){
            this.gameEvents = new ArrayList<>();
        }

        this.gameEvents.add(event);
    }

    public void addPlayer(Player player){
        if(this.players == null){
            this.players = new ArrayList<>();
        }

        this.players.add(player);

        player.setTeam(this);
    }

}
