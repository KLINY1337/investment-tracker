package com.fintracker.backend.fintrackermonolith.auth_server.configuration;

import com.fintracker.backend.fintrackermonolith.auth_server.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class AuthServerConfiguration {

    private final UserRepository userRepository;

    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findUserByUsernameOrEmail(username).orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
    }
}
