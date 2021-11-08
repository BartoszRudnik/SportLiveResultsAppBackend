package com.example.demo.team;

import com.example.demo.appUser.AppUser;
import com.example.demo.gameEvent.GameEvent;
import com.example.demo.league.League;
import com.example.demo.leagueTable.LeagueTable;
import com.example.demo.player.Player;
import com.example.demo.socialMedia.SocialMedia;
import lombok.*;

import javax.persistence.*;
import java.util.*;

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

    @OneToOne
    @JoinColumn(name = "league_table_id")
    private LeagueTable leagueTable;

    @OneToOne
    @JoinColumn(name = "social_media_id")
    private SocialMedia socialMedia;

    @ManyToMany(mappedBy = "favoriteGames")
    private Set<AppUser> users;

    public Team(String teamName, String city, String stadiumName, List<Player> players, League league){
        this.teamName = teamName;
        this.city = city;
        this.stadiumName = stadiumName;
        this.players = players;
        this.league = league;
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
        Team team = (Team) o;
        return id.equals(team.id) && teamName.equals(team.teamName) && Objects.equals(city, team.city) && Objects.equals(stadiumName, team.stadiumName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teamName, city, stadiumName);
    }
}
