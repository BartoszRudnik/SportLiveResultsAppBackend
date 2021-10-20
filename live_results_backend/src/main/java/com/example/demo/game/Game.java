package com.example.demo.game;

import com.example.demo.appUser.AppUser;
import com.example.demo.gameEvent.GameEvent;
import com.example.demo.gamePlayer.GamePlayer;
import com.example.demo.league.League;
import com.example.demo.team.Team;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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

    private LocalDateTime gameStartDate;
    private LocalDateTime actualStartDate;

    private GameStatus gameStatus;
    private int round;

    @OneToMany(mappedBy = "game")
    private List<GameEvent> gameEvents;

    @ManyToMany(mappedBy = "favoriteGames")
    private Set<AppUser> users;

    @OneToMany(mappedBy = "game")
    private Set<GamePlayer> players;

    public Game(int scoreTeamA, int scoreTeamB, League league, Team teamA, Team teamB, LocalDateTime actualStartDate, LocalDateTime gameStartDate, GameStatus gameStatus, int round){
        this.scoreTeamA = scoreTeamA;
        this.scoreTeamB = scoreTeamB;
        this.league = league;
        this.teamA = teamA;
        this.teamB = teamB;
        this.gameStartDate = gameStartDate;
        this.gameStatus = gameStatus;
        this.round = round;
        this.actualStartDate = actualStartDate;
    }

    public Game(int scoreTeamA, int scoreTeamB, League league, Team teamA, Team teamB,  LocalDateTime actualStartDate, LocalDateTime gameStartDate, GameStatus gameStatus, List<GameEvent> gameEvents, int round){
        this.scoreTeamA = scoreTeamA;
        this.scoreTeamB = scoreTeamB;
        this.league = league;
        this.teamA = teamA;
        this.teamB = teamB;
        this.gameStartDate = gameStartDate;
        this.gameStatus = gameStatus;
        this.gameEvents = gameEvents;
        this.round = round;
        this.actualStartDate = actualStartDate;
    }

    public void addGameEvent(GameEvent event){
        if(this.gameEvents == null){
            this.gameEvents = new ArrayList<>();
        }

        this.gameEvents.add(event);
    }

    public void addPlayer(GamePlayer gamePlayer){
        if(this.players == null){
            this.players = new HashSet<>();
        }

        this.players.add(gamePlayer);
        gamePlayer.setGame(this);
    }

    public void removePlayer(GamePlayer player){
        if(this.players != null){
            this.players.remove(player);
        }
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
        Game game = (Game) o;
        return round == game.round && id.equals(game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, round);
    }
}
