package com.aditya.springsecurity.security;

import com.aditya.springsecurity.auth.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.aditya.springsecurity.security.ApplicationUserRole.STUDENT;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {

    private final ApplicationUserService applicationUserService;


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
        }).formLogin(formLogin -> formLogin.loginPage("/login").permitAll().defaultSuccessUrl("/courses", true).usernameParameter("username").passwordParameter("password")).rememberMe(validity -> validity.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)).key("somethingverysecured").rememberMeParameter("remember-me")).logout(logout -> logout.logoutUrl("/logout").logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).clearAuthentication(true).invalidateHttpSession(true).deleteCookies("JSESSIONID", "remember-me").logoutSuccessUrl("/login"));
        return http.build();
    }


    /*
   // In memory database
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
   */

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(applicationUserService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

}
