package com.example.demo.appUser;

import com.example.demo.game.Game;
import com.example.demo.league.League;
import com.example.demo.team.Team;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class AppUser implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;

    private Boolean locked = false;
    private Boolean enabled = true;

    @ManyToMany
    @JoinTable(
            name = "notification_games",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private Set<Game> notificationGames;

    @ManyToMany
    @JoinTable(
            name = "favorite_games",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private Set<Game> favoriteGames;

    @ManyToMany
    @JoinTable(
            name = "favorite_leagues",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "league_id")
    )
    private Set<League> favoriteLeagues;

    @ManyToMany
    @JoinTable(
            name = "favorite_teams",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<Team> favoriteTeams;

    public AppUser(String firstName, String lastName, String email, AppUserRole appUserRole){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.appUserRole = appUserRole;
    }

    public AppUser(String firstName, String lastName, String email, String password, AppUserRole appUserRole) {
        this.firstName = firstName;
        this.lastName =lastName;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
    }

    @Override
    public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.appUserRole.name());

        return Collections.singletonList(authority);
    }

    @Override
    public java.lang.String getPassword() {
        return this.password;
    }

    @Override
    public java.lang.String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void addNotificationGame(Game game){
        if(this.notificationGames == null){
            this.notificationGames = new HashSet<>();
        }

        this.notificationGames.add(game);
        game.addNotificationUser(this);
    }

    public void removeNotificationGame(Game game){
        if(this.notificationGames != null){
            this.notificationGames.remove(game);
            game.removeNotificationUser(this);
        }
    }

    public void addFavoriteGame(Game game){
        if(this.favoriteGames == null){
            this.favoriteGames = new HashSet<>();
        }

        this.favoriteGames.add(game);
        game.addFavoriteUser(this);
    }

    public void removeFavoriteGame(Game game){
        if(this.favoriteGames != null){
            this.favoriteGames.remove(game);
            game.removeFavoriteUser(this);
        }
    }

    public void addFavoriteTeam(Team team){
        if(this.favoriteTeams == null){
            this.favoriteTeams = new HashSet<>();
        }

        this.favoriteTeams.add(team);
        team.addUser(this);
    }

    public void removeFavoriteTeam(Team team){
        if(this.favoriteTeams != null){
            this.favoriteTeams.remove(team);
            team.removeUser(this);
        }
    }

    public void addFavoriteLeague(League league){
        if(this.favoriteLeagues == null){
            this.favoriteLeagues = new HashSet<>();
        }

        this.favoriteLeagues.add(league);
        league.addUser(this);
    }

    public void removeFavoriteLeague(League league){
        if(this.favoriteLeagues != null){
            this.favoriteLeagues.remove(league);
            league.removeUser(this);
        }
    }
}
