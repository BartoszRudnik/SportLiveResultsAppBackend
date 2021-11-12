package com.example.demo.signIn;

import com.example.demo.confirmationToken.ConfirmationToken;
import com.example.demo.signIn.dto.SignInAnon;
import com.example.demo.signIn.dto.SignInRequest;
import com.example.demo.signUp.dto.ResponseToken;
import com.example.demo.signUp.dto.SignUpRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/signIn")
@AllArgsConstructor
public class SignInController {

    private final SignInService signInService;

    @PostMapping("/signIn")
    public ResponseToken signIn(@RequestBody SignInRequest request){
        ConfirmationToken token = this.signInService.signIn(request);

        return new ResponseToken(token.getToken(), Duration.between(token.getCreatedAt(), token.getExpiresAt()).toMillis());
    }

    @PostMapping("/googleSignIn")
    public ResponseToken googleSignIn(@RequestBody SignUpRequest request){
        ConfirmationToken token = this.signInService.googleSignIn(request);

        return new ResponseToken(token.getToken(), Duration.between(token.getCreatedAt(), token.getExpiresAt()).toMillis());
    }

    @PostMapping("/anonSignIn")
    public ResponseToken anonSignIn(@RequestBody SignInAnon request){
        ConfirmationToken token = this.signInService.anonSignIn(request);

        return new ResponseToken(token.getToken(), Duration.between(token.getCreatedAt(), token.getExpiresAt()).toMillis());
    }

}
