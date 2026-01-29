package com.okayji.identity.service;

import com.okayji.identity.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public record CustomUserDetailsService(UserRepository userRepository) {
    public UserDetailsService userDetailsService() {
        return userRepository::findByUsernameIgnoreCase;
    }
}
