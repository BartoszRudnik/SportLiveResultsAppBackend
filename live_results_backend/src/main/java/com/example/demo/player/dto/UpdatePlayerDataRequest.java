package com.example.demo.player.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatePlayerDataRequest {
    String firstName;
    String lastName;
    String position;
}
