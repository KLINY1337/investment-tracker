package com.fintracker.backend.fintrackermonolith.auth.service;

import com.fintracker.backend.fintrackermonolith.auth.controller.response.LoginResponse;
import com.fintracker.backend.fintrackermonolith.user.entity.User;
import com.fintracker.backend.fintrackermonolith.auth.exception.UserAlreadyExistsException;
import com.fintracker.backend.fintrackermonolith.auth.repository.UserRepository;
import com.fintracker.backend.fintrackermonolith.auth.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public User signup(String username, String email, String password) {
        if (userRepository.existsByUsernameOrEmail(username, email)) {
            throw new UserAlreadyExistsException("Specified username or email is already occupied");
        }
        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        return userRepository.save(user);
    }

    public LoginResponse authenticate(String usernameOrEmail, String password) {
        Optional<User> userOptional = userRepository.findUserByUsernameOrEmail(usernameOrEmail);
        AtomicReference<LoginResponse> responseReference = new AtomicReference<>(new LoginResponse(
                false,
                "Authentication error",
                null,
                null
        ));
        userOptional.ifPresentOrElse(
                user -> {
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    user.getUsername(),
                                    password
                            )
                    );
                    String jwtToken = jwtService.generateToken(user);
                    responseReference.set(new LoginResponse(
                            true,
                            "User successfully authenticated",
                            jwtToken,
                            jwtService.getExpirationTime()
                    ));
                },
                () -> authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                usernameOrEmail,
                                password
                        )
                )
        );
        return responseReference.get();
    }
}
