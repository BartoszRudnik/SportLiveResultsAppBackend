package com.example.demo.confirmationToken;

import com.example.demo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);

    @Transactional
    void deleteAllByUser(User user);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    void updateConfirmedAt(String token, LocalDateTime confirmedAt);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.token = ?1, c.createdAt = ?2, c.expiresAt = ?3 " +
            "WHERE c.user.id = ?4")
    void updateToken (String token, LocalDateTime createdAt, LocalDateTime expiresAt, Long id);


}
