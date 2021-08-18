package com.example.demo.user;

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
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with email %s not found";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    public Optional<User> findByEmail(String email){
        return this.userRepository.findByEmail(email);
    }

    public void deleteUser(User user){
        this.userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepository.
                findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public ConfirmationToken signInUser(String email, String password){

        boolean userExists = this.userRepository.
                findByEmail(email).
                isPresent();

        if(!userExists){
            throw new IllegalStateException("User doesn't exist");
        }

        User user = this.userRepository.findByEmail(email).get();

        if(!this.bCryptPasswordEncoder.matches(password, user.getPassword()) || !user.getEnabled()){
            throw new IllegalStateException("Wrong email or password");
        }

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        this.confirmationTokenService.updateConfirmationToken(confirmationToken);

        return confirmationToken;
    }

    public ConfirmationToken googleSignIn(SignUpRequest request){

        boolean userExists = this.userRepository
                .findByEmail(request.getEmail())
                .isPresent();

        if(!userExists){
            UserRole userRole;

            if(request.getUserType().equals("google")){
                userRole = UserRole.GOOGLE_USER;
            }else{
                userRole = UserRole.MAIL_USER;
            }

            User user = new User(request.getFirstName(), request.getLastName(), request.getEmail(), userRole);

            this.userRepository.save(user);
        }

        User user = this.userRepository.findByEmail(request.getEmail()).get();

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
          token,
          LocalDateTime.now(),
          LocalDateTime.now().plusMinutes(15),
          user
        );

        this.confirmationTokenService.updateConfirmationToken(confirmationToken);

        return confirmationToken;
    }

    public ConfirmationToken signUpUser(User user){

        boolean userExists = this.userRepository
                .findByEmail(user.getEmail())
                .isPresent();

        if(userExists){
            throw new IllegalStateException("Email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        this.userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
          token,
          LocalDateTime.now(),
          LocalDateTime.now().plusMinutes(15),
          user
        );

        this.confirmationTokenService.saveConfirmationToken(confirmationToken);

        return confirmationToken;
    }

    public void enableUser(String email){
        this.userRepository.enableAppUser(email);
    }

    public void disableUser(String email){
        this.userRepository.disableAppUser(email);
    }

    public void updateUserPassword(String email, String newPassword){
        this.userRepository.updateAppUserPassword(email, newPassword);
    }

}
