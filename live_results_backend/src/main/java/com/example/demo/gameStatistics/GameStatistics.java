package com.example.demo.gameStatistics;

import com.example.demo.game.Game;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "GameStatistics")
public class GameStatistics {
    @Id
    @SequenceGenerator(
            name = "game_statistics_sequence",
            sequenceName = "game_statistics_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "game_statistics_sequence"
    )
    private Long id;

    @OneToOne
    @JoinColumn(name = "game_id")
    private Game game;

    private int foulsTeamA;
    private int foulsTeamB;
    private int cornersTeamA;
    private int cornersTeamB;
    private int shootsTeamA;
    private int shootsTeamB;
    private int offsidesTeamA;
    private int offsidesTeamB;

    GameStatistics(Game game){
        this.game = game;
    }

    GameStatistics(Game game, int foulsTeamA, int foulsTeamB, int cornersTeamA, int cornersTeamB, int shootsTeamA, int shootsTeamB, int offsidesTeamA, int offsidesTeamB){
        this.game = game;
        this.foulsTeamA = foulsTeamA;
        this.foulsTeamB = foulsTeamB;
        this.cornersTeamA = cornersTeamA;
        this.cornersTeamB = cornersTeamB;
        this.shootsTeamA = shootsTeamA;
        this.shootsTeamB = shootsTeamB;
        this.offsidesTeamA = offsidesTeamA;
        this.offsidesTeamB = offsidesTeamB;
    }
}
