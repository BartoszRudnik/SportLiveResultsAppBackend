package com.example.demo.passwordReset;


import com.example.demo.confirmationToken.ConfirmationToken;
import com.example.demo.passwordReset.dto.NewPasswordRequest;
import com.example.demo.signUp.dto.ResponseToken;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/passwordReset")
@AllArgsConstructor
public class PasswordResetController {

    private final PasswordResetService newPasswordService;

    @PostMapping("/reset/{email}")
    public String reset(@PathVariable String email){
        this.newPasswordService.resetPassword(email);

        return "Success";
    }

    @PostMapping("/newPassword")
    public ResponseToken newPassword(@RequestBody NewPasswordRequest request){
        ConfirmationToken token = this.newPasswordService.newPassword(request);

        return new ResponseToken(token.getToken(), Duration.between(token.getCreatedAt(), token.getExpiresAt()).toMillis());
    }

}
