package com.example.demo.signIn.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SignInRequest {

    private final String password;
    private final String email;
    private final String userType;

}
