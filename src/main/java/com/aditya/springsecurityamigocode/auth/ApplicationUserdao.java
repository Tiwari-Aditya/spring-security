package com.aditya.springsecurityamigocode.auth;

import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import java.util.Optional;

public interface ApplicationUserdao{
    Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}
