package com.example.demo.message;

import com.example.demo.appUser.AppUser;
import com.example.demo.game.Game;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="message")
public class Message {

    @Id
    @SequenceGenerator(
            name = "message_sequence",
            sequenceName = "message_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "message_sequence"
    )
    private Long id;

    private String text;
    private LocalDateTime dateTime;
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "parent_message")
    private Message parentMessage;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    public Message(String text, LocalDateTime dateTime, Game game, AppUser appUser, Message parentMessage){
        this.text = text;
        this.dateTime = dateTime;
        this.game = game;
        this.appUser = appUser;
        this.deleted = false;
        this.parentMessage = parentMessage;
    }

    public Message(String text, LocalDateTime dateTime, Game game, AppUser appUser){
        this.text = text;
        this.dateTime = dateTime;
        this.game = game;
        this.appUser = appUser;
        this.deleted = false;
    }
}
