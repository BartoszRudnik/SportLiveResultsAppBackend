package com.example.demo.signIn;

import com.example.demo.confirmationToken.ConfirmationToken;
import com.example.demo.signIn.dto.SignInRequest;
import com.example.demo.signUp.dto.SignUpRequest;
import com.example.demo.appUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SignInService {

    private final AppUserService appUserService;

    public ConfirmationToken signIn(SignInRequest request){
        return this.appUserService.signInUser(request.getEmail(), request.getPassword());
    }

    public ConfirmationToken googleSignIn(SignUpRequest request){
        return this.appUserService.googleSignIn(request);
    }

}
