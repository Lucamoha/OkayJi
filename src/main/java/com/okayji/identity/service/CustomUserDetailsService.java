package com.okayji.identity.service;

import com.okayji.identity.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public record CustomUserDetailsService(UserRepository userRepository) {
    public UserDetailsService userDetailsService() {
        return username -> Objects
                .requireNonNull(userRepository.findUserByIdOrUsername(username, username)
                        .orElse(null));
    }
}
