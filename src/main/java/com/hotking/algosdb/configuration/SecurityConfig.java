package com.hotking.algosdb.configuration;

import com.hotking.algosdb.entity.User;
import com.hotking.algosdb.enums.Role;
import com.hotking.algosdb.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService(UserRepository userRepo){
//        return mailOrUsername -> {
//            User user = userRepo.findByUsername(mailOrUsername);
//            if(user.getRole() == Role.ADMIN){
//
//            }
//            if(user == null) user = userRepo.findByEmail(mailOrUsername);
//            if(user != null) return user;
//
//            throw new UsernameNotFoundException("User + '" + mailOrUsername + "' not found");
//        };
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/algo/**", "/logout", "/confirmCode", "/confirm-admin").permitAll()
                        .requestMatchers("/management/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .disable())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/login")
                        .permitAll())
                .build();
    }
}
