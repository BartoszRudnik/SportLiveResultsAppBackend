package com.example.demo.passwordReset;

import com.example.demo.appUser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    Optional<ResetToken> findByToken(String token);

    ResetToken findByTokenAndAppUser(String token, AppUser user);

    @Transactional
    void deleteAllByAppUser(AppUser appUser);

    @Transactional
    @Modifying
    void deleteByAppUser(AppUser appUser);

}
