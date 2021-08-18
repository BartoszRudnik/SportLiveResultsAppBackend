package com.example.demo.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collections;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="User")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long userId;

    private String firstName;
    private String lastName;
    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private Boolean locked;
    private Boolean enabled;

    public User(String firstName, String lastName, String email, UserRole userRole){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userRole = userRole;
    }

    public User(String firstName, String lastName, String email, String password, UserRole userRole) {
        this.firstName = firstName;
        this.lastName =lastName;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    @Override
    public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.userRole.name());

        return Collections.singletonList(authority);
    }

    @Override
    public java.lang.String getPassword() {
        return this.password;
    }

    @Override
    public java.lang.String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
