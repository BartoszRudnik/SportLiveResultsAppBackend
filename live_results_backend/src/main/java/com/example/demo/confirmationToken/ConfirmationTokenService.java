package com.example.demo.confirmationToken;

import com.example.demo.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token){
        this.confirmationTokenRepository.save(token);
    }

    public void updateConfirmationToken(ConfirmationToken token){
        this.confirmationTokenRepository.updateToken(token.getToken(), token.getCreatedAt(), token.getExpiresAt(), token.getUser().getUserId());
    }

    public Optional<ConfirmationToken> getToken(String token){
        return this.confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token){
        this.confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    public void deleteConfirmationToken(User user){
        this.confirmationTokenRepository.deleteAllByUser(user);
    }

}

