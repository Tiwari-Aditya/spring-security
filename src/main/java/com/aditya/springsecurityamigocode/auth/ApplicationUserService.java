package com.aditya.springsecurityamigocode.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {
    private final ApplicationUserdao applicationUserdao;
    @Autowired
    public ApplicationUserService( @Qualifier("fake")ApplicationUserdao applicationUserdao) {
        this.applicationUserdao = applicationUserdao;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserdao.selectApplicationUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found ", username)));
    }
}
