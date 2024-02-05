package com.aditya.springsecurityamigocode.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.aditya.springsecurityamigocode.security.ApplicationUserRole.*;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf-> csrf.disable())
                .authorizeHttpRequests((requests) -> {
                            requests.requestMatchers("/hello/**").permitAll()
                            .requestMatchers("/api/**").hasRole(STUDENT.name());
            requests.anyRequest().authenticated();
        }).httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails annaSmithUser = User.builder()
                .username("annasmith")
                .password(passwordEncoder().encode("password"))
                .roles(STUDENT.name()).build();

        UserDetails lindaUser = User.builder()
                .username("linda")
                .password(passwordEncoder().encode("password123"))
                .roles(ADMIN.name()).build();

        UserDetails tomUser = User.builder()
                .username("tom")
                .password(passwordEncoder().encode("password1"))
                .roles(ADMINTRAINEE.name()).build();

        return new InMemoryUserDetailsManager(annaSmithUser,lindaUser,tomUser);
    }
}
