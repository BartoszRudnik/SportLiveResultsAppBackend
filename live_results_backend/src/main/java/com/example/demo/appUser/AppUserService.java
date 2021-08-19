package com.example.demo.appUser;

import com.example.demo.confirmationToken.ConfirmationToken;
import com.example.demo.confirmationToken.ConfirmationTokenService;
import com.example.demo.signUp.dto.SignUpRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with email %s not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    public Optional<AppUser> findByEmail(String email){
        return this.appUserRepository.findByEmail(email);
    }

    public void deleteUser(AppUser appUser){
        this.appUserRepository.delete(appUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.appUserRepository.
                findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public ConfirmationToken signInUser(String email, String password){

        boolean userExists = this.appUserRepository.
                findByEmail(email).
                isPresent();

        if(!userExists){
            throw new IllegalStateException("User doesn't exist");
        }

        AppUser appUser = this.appUserRepository.findByEmail(email).get();

        if(!this.bCryptPasswordEncoder.matches(password, appUser.getPassword()) || !appUser.getEnabled()){
            throw new IllegalStateException("Wrong email or password");
        }

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        this.confirmationTokenService.updateConfirmationToken(confirmationToken);

        return confirmationToken;
    }

    public ConfirmationToken googleSignIn(SignUpRequest request){

        boolean userExists = this.appUserRepository
                .findByEmail(request.getEmail())
                .isPresent();

        if(!userExists){
            AppUserRole appUserRole;

            if(request.getUserType().equals("google")){
                appUserRole = AppUserRole.GOOGLE_USER;
            }else{
                appUserRole = AppUserRole.MAIL_USER;
            }

            AppUser appUser = new AppUser(request.getFirstName(), request.getLastName(), request.getEmail(), appUserRole);

            this.appUserRepository.save(appUser);
        }

        AppUser appUser = this.appUserRepository.findByEmail(request.getEmail()).get();

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
          token,
          LocalDateTime.now(),
          LocalDateTime.now().plusMinutes(15),
                appUser
        );

        this.confirmationTokenService.updateConfirmationToken(confirmationToken);

        return confirmationToken;
    }

    public ConfirmationToken signUpUser(AppUser appUser){

        boolean userExists = this.appUserRepository.findByEmail(appUser.getEmail()).isPresent();

        if(userExists){
            throw new IllegalStateException("Email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        this.appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
          token,
          LocalDateTime.now(),
          LocalDateTime.now().plusMinutes(15),
                appUser
        );

        this.confirmationTokenService.saveConfirmationToken(confirmationToken);

        return confirmationToken;
    }

    public void enableUser(String email){
        this.appUserRepository.enableAppUser(email);
    }

    public void disableUser(String email){
        this.appUserRepository.disableAppUser(email);
    }

    public void updateUserPassword(String email, String newPassword){
        this.appUserRepository.updateAppUserPassword(email, newPassword);
    }

}
