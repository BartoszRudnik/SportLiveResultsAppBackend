package com.example.demo.security;
import com.example.demo.appUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.authorizeRequests().anyRequest().permitAll();
//        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
          http.csrf().disable().authorizeRequests().
                antMatchers("/api/v*/registration/**").permitAll().
                antMatchers("/api/v*/signIn/**").permitAll().
                antMatchers("/api/v*/resetToken/**").permitAll().
                antMatchers("/api/v*/newPassword/**").permitAll().
                antMatchers("/api/v*/passwordReset/**").permitAll().
                antMatchers("/api/v*/team/**").permitAll().
                antMatchers("/api/v*/game/**").permitAll().
                antMatchers("/api/v*/player/**").permitAll().
                antMatchers("/api/v*/league/**").permitAll().
                antMatchers("/api/v*/user/**").permitAll().
                antMatchers("/api/v*/gameEvent/**").permitAll().
                antMatchers("/api/v*/notification/**").permitAll().
                antMatchers("/api/v*/gameStatistics/**").permitAll().
                antMatchers("/api/v*/fcm/**").permitAll().
                antMatchers("/api/v*/message/**").permitAll().
                antMatchers("/api/v*/report/**").permitAll().
                anyRequest().
                authenticated().and()
                .formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);

        return provider;
    }

}
