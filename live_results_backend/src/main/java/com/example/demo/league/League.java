package com.example.demo.league;

import com.example.demo.game.Game;
import com.example.demo.team.Team;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="League")
public class League {

    @Id
    @SequenceGenerator(
            name = "league_sequence",
            sequenceName = "league_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "league_sequence"
    )
    private Long leagueId;

    private String leagueName;

    private List<Team> teams;
    private List<Game> games;

    private LeagueTable leagueTable;

}
