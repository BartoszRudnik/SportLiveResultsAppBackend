package com.example.demo.passwordReset;

import com.example.demo.appUser.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ResetTokenService {

    private final ResetTokenRepository resetTokenRepository;

    public void saveResetToken(ResetToken token){
        resetTokenRepository.save(token);
    }

    public void deleteToken(AppUser appUser){
        resetTokenRepository.deleteByAppUser(appUser);
    }

    public Optional<ResetToken> getToken(String token){
        return resetTokenRepository.findByToken(token);
    }

    public ResetToken getTokenByTokenAndUser(AppUser user, String token){
        return resetTokenRepository.findByTokenAndAppUser(token, user);
    }

    public void deleteUserTokens(AppUser user){
        resetTokenRepository.deleteAllByAppUser(user);
    }

}
