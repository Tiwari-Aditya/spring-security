package com.aditya.springsecurity.auth;

import java.util.Optional;

public interface ApplicationUserdao{
    Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}
