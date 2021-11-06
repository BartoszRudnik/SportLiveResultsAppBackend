package com.example.demo.game;

import com.example.demo.appUser.AppUser;
import com.example.demo.gameEvent.GameEvent;
import com.example.demo.gamePlayer.GamePlayer;
import com.example.demo.gameStatistics.GameStatistics;
import com.example.demo.league.League;
import com.example.demo.team.Team;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
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

    @OneToOne
    @JoinColumn(name = "game_statistics_id")
    private GameStatistics gameStatistics;

    private LocalDateTime gameStartDate;
    private LocalDateTime actualStartDate;
    private GameStatus gameStatus;
    private int round;

    @Column
    private boolean isBreak;

    @Column
    private int partOfGame;

    @Column
    private int lengthOfPartOfGame;

    @OneToMany(mappedBy = "game")
    private List<GameEvent> gameEvents;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser reporter;

    @ManyToMany(mappedBy = "favoriteGames")
    private Set<AppUser> favoriteUsers;

    @ManyToMany(mappedBy = "notificationGames")
    private Set<AppUser> notificationUsers;

    @OneToMany(mappedBy = "game")
    private Set<GamePlayer> players;

    public Game(int scoreTeamA, int scoreTeamB, League league, Team teamA, Team teamB, LocalDateTime actualStartDate, LocalDateTime gameStartDate, GameStatus gameStatus, int round, GameStatistics gameStatistics){
        this.scoreTeamA = scoreTeamA;
        this.scoreTeamB = scoreTeamB;
        this.league = league;
        this.teamA = teamA;
        this.teamB = teamB;
        this.gameStartDate = gameStartDate;
        this.gameStatus = gameStatus;
        this.round = round;
        this.actualStartDate = actualStartDate;
        this.isBreak = false;
        this.partOfGame = 0;
        this.lengthOfPartOfGame = 45;
        this.gameStatistics = gameStatistics;
    }

    public Game(int scoreTeamA, int scoreTeamB, League league, Team teamA, Team teamB, LocalDateTime actualStartDate, LocalDateTime gameStartDate, GameStatus gameStatus, List<GameEvent> gameEvents, int round, GameStatistics gameStatistics){
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
        this.isBreak = false;
        this.partOfGame = 0;
        this.lengthOfPartOfGame = 45;
        this.gameStatistics = gameStatistics;
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

    public void addNotificationUser(AppUser user){
        if(this.notificationUsers == null){
            this.notificationUsers = new HashSet<>();
        }

        this.notificationUsers.add(user);
    }

    public void removeNotificationUser(AppUser user){
        if(this.notificationUsers != null){
            this.notificationUsers.remove(user);
        }
    }

    public void addFavoriteUser(AppUser user){
        if(this.favoriteUsers == null){
            this.favoriteUsers = new HashSet<>();
        }

        this.favoriteUsers.add(user);
    }

    public void removeFavoriteUser(AppUser user){
        if(this.favoriteUsers != null){
            this.favoriteUsers.remove(user);
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
