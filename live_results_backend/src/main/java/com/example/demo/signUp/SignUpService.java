package com.example.demo.signUp;

import com.example.demo.confirmationToken.ConfirmationToken;
import com.example.demo.confirmationToken.ConfirmationTokenService;
import com.example.demo.emailSender.EmailSender;
import com.example.demo.signUp.dto.SignUpRequest;
import com.example.demo.appUser.AppUser;
import com.example.demo.appUser.AppUserRole;
import com.example.demo.appUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SignUpService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    public ConfirmationToken register(SignUpRequest request){
        boolean isValidEmail = this.emailValidator.test(request.getEmail());

        if(!isValidEmail){
            throw new IllegalStateException("Email is not valid");
        }

        AppUser newAppUser = new AppUser(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(), AppUserRole.MAIL_USER);

        ConfirmationToken token = this.appUserService.signUpUser(newAppUser);

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token.getToken();

        emailSender.send("Confirm your registration", request.getEmail(), emailSender.buildEmailRegistration(request.getFirstName(), link));

        return token;
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = this.confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        if(confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("Email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("Token has expired");
        }

        this.confirmationTokenService.setConfirmedAt(token);

        this.appUserService.enableUser(confirmationToken.getAppUser().getEmail());

        return "confirmed";
    }

}
