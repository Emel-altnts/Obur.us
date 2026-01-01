package com.project.Obur.us.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        return User.builder()
                .username(username)
                .password("") // JWT olduğu için password check yok
                .roles("USER")
                .build();
    }
}
