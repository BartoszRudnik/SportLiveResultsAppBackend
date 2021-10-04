package com.example.demo.appUser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserFavoritesRequest {
    private List<Long> games;
    private List<Long> teams;
    private List<Long> leagues;
}
