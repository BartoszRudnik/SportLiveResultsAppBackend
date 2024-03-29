package com.example.demo.league;

import com.example.demo.appUser.AppUser;
import com.example.demo.game.Game;
import com.example.demo.team.Team;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
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
    private Long id;

    private String leagueName;

    @OneToMany(mappedBy = "league")
    private List<Team> teams;

    @OneToMany(mappedBy = "league")
    private List<Game> games;

    @ManyToMany(mappedBy = "favoriteGames")
    private Set<AppUser> users;

    private LeagueLevel leagueLevel;

    public League(String leagueName){
        this.leagueName = leagueName;
    }

    public League(String leagueName, LeagueLevel leagueLevel){
        this.leagueName = leagueName;
        this.leagueLevel = leagueLevel;
    }

    public League(String leagueName, List<Team> teams, List<Game> games){
        this.leagueName = leagueName;
        this.teams = teams;
        this.games = games;
    }

    void addGame(Game game){
        if(this.games == null){
            this.games = new ArrayList<>();
        }

        this.games.add(game);

        game.setLeague(this);
    }

    void addTeam(Team team){
        if(this.teams == null){
            this.teams = new ArrayList<>();
        }

        this.teams.add(team);

        team.setLeague(this);
    }

    public void addUser(AppUser user){
        if(this.users == null){
            this.users = new HashSet<>();
        }

        this.users.add(user);
    }

    public void removeUser(AppUser user){
        if(this.users != null){
            this.users.remove(user);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        League league = (League) o;
        return id.equals(league.id) && leagueName.equals(league.leagueName) && leagueLevel == league.leagueLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, leagueName, leagueLevel);
    }
}
