package com.example.demo.leagueTable;

import com.example.demo.league.League;
import com.example.demo.team.Team;
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
@Entity(name="League_Table")
public class LeagueTable implements Comparable<LeagueTable>{

    @Id
    @SequenceGenerator(
            name = "league_table_sequence",
            sequenceName = "league_table_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "league_table_sequence"
    )
    private Long id;

    private int games;
    private int goalsScored;
    private int goalsConceded;
    private int points;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public LeagueTable(Team team){
        this.team = team;
    }

    @Override
    public int compareTo(LeagueTable u) {
        return Integer.compare(this.points, u.points);
    }
}
