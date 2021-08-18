package com.example.demo.signUp;

import com.example.demo.confirmationToken.ConfirmationToken;
import com.example.demo.signUp.dto.ResponseToken;
import com.example.demo.signUp.dto.SignUpRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/register")
    public ResponseToken signUp(@RequestBody SignUpRequest request){
      ConfirmationToken token = this.signUpService.register(request);

      return new ResponseToken(token.getToken(), Duration.between(token.getCreatedAt(), token.getExpiresAt()).toMillis());
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam String token){
        return  this.signUpService.confirmToken(token);
    }

}
