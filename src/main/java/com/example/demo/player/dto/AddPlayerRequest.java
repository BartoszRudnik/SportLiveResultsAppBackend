package com.example.demo.player.dto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AddPlayerRequest {

    private String firstName;
    private String lastName;
    private String position;
    private int numberOfGoals;
    private int numberOfAssists;
    private Long teamId;

}
