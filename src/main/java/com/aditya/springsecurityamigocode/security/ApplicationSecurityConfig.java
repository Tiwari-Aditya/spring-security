package com.aditya.springsecurityamigocode.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.aditya.springsecurityamigocode.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
/*
// Users Roles and Authorities
    @Bean
    SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests((requests) -> {
            requests.requestMatchers("/hello/**").permitAll().requestMatchers("/api/**").hasRole(STUDENT.name())
                       .requestMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                       .requestMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                       .requestMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                       .requestMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name());
            requests.anyRequest().authenticated();
        }).httpBasic(withDefaults());
        return http.build();
    }
*/


/*

// Permission based Authentication
    @Bean
    SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests((requests) -> {
            requests.anyRequest().authenticated();
        }).httpBasic(withDefaults());
        return http.build();
    }

*/

    //Form Based Authentication
    @Bean
    SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests((requests) -> {
            requests.requestMatchers("/hello/**").permitAll().requestMatchers("/api/**").hasRole(STUDENT.name()).anyRequest().authenticated();
        }).formLogin(formLogin -> formLogin.loginPage("/login").permitAll().defaultSuccessUrl("/courses", true)).rememberMe(validity -> validity.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)).key("somethingverysecured")).logout(logout -> logout.logoutUrl("/logout").logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).clearAuthentication(true).invalidateHttpSession(true).deleteCookies("JSESSIONID", "remember-me").logoutSuccessUrl("/login"));
        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails annaSmithUser = User.builder().username("annasmith").password(passwordEncoder().encode("password"))
//                .roles(STUDENT.name())
                .authorities(STUDENT.getGrantedAuthorities()).build();

        UserDetails lindaUser = User.builder().username("linda").password(passwordEncoder().encode("password123")).roles(ADMIN.name()).authorities(ADMIN.getGrantedAuthorities()).build();

        UserDetails tomUser = User.builder().username("tom").password(passwordEncoder().encode("password1"))
//                .roles(ADMINTRAINEE.name())
                .authorities(ADMINTRAINEE.getGrantedAuthorities()).build();

        return new InMemoryUserDetailsManager(annaSmithUser, lindaUser, tomUser);
    }
}
