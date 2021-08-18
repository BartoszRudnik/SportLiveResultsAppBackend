package com.example.demo.signIn;

import com.example.demo.confirmationToken.ConfirmationToken;
import com.example.demo.signIn.dto.SignInRequest;
import com.example.demo.signUp.dto.SignUpRequest;
import com.example.demo.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SignInService {

    private final UserService userService;

    public ConfirmationToken signIn(SignInRequest request){
        return this.userService.signInUser(request.getEmail(), request.getPassword());
    }

    public ConfirmationToken googleSignIn(SignUpRequest request){
        return this.userService.googleSignIn(request);
    }

}
