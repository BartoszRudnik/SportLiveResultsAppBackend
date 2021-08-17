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
public class LeagueTable {

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
    private Long leagueTableId;

    private int games;
    private int goalsScored;
    private int goalsConceded;
    private int points;

    @OneToMany(mappedBy = "leagueTable")
    private List<Team> teams;

    @OneToOne
    @JoinColumn(name = "league_id")
    private League league;

    void addTeam(Team team){
        if(this.teams == null){
            this.teams = new ArrayList<>();
        }

        this.teams.add(team);

        team.setLeagueTable(this);
    }

}
