package com.example.demo.passwordReset;

import com.example.demo.appUser.AppUser;
import com.example.demo.appUser.AppUserService;
import com.example.demo.confirmationToken.ConfirmationToken;
import com.example.demo.confirmationToken.ConfirmationTokenService;
import com.example.demo.emailSender.EmailSender;
import com.example.demo.passwordReset.dto.NewPasswordRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetService {

    private final AppUserService appUserService;
    private final ResetTokenService resetTokenService;
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private String generateResetToken(){
        int tokenLen = 6;

        String digits = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(tokenLen);

        for(int i = 0; i < tokenLen; i++) {
            stringBuilder.append(digits.charAt(random.nextInt(digits.length())));
        }

        return stringBuilder.toString();
    }

    public void resetPassword(String email){
        boolean userExists = this.appUserService
                .findByEmail(email)
                .isPresent();

        if(!userExists){
            throw new IllegalStateException("Wrong email or password");
        }

        this.appUserService.disableUser(email);

        AppUser user = this.appUserService.findByEmail(email).get();

        String code = generateResetToken();

        ResetToken resetToken = new ResetToken(
                code, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);

        resetTokenService.saveResetToken(resetToken);

        emailSender.send("Reset password",email, emailSender.buildEmailPasswordReset(user.getFirstName(), code));
    }

    public boolean checkTokenExpiryDate(ResetToken resetToken){
        return resetToken.getExpiresAt().isAfter(LocalDateTime.now());
    }

    public ConfirmationToken newPassword(NewPasswordRequest request){

        boolean userExists = this.appUserService.findByEmail(request.getEmail()).isPresent();
        boolean tokenExists = this.resetTokenService.getToken(request.getToken()).isPresent();

        if(!userExists){
            throw new IllegalStateException("User doesn't exists");
        }

        if(!tokenExists){
            throw new IllegalStateException("Wrong token");
        }

        AppUser user = this.appUserService.findByEmail(request.getEmail()).get();
        ResetToken savedToken = this.resetTokenService.getToken(request.getToken()).get();

        if(!Objects.equals(user.getId(), savedToken.getAppUser().getId())){
            throw new IllegalStateException("Token doesn't match with user");
        }

        if(!this.checkTokenExpiryDate(savedToken)){
            throw new IllegalStateException("Token has expired");
        }

        String encodedPassword = this.bCryptPasswordEncoder.encode(request.getNewPassword());

        this.appUserService.updateUserPassword(request.getEmail(), encodedPassword);
        this.appUserService.enableUser(request.getEmail());

        String responseToken = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                responseToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        this.confirmationTokenService.updateConfirmationToken(confirmationToken);
        this.resetTokenService.deleteToken(user);

        return confirmationToken;
    }

}
