package com.fintracker.backend.auth_microservice.authentication.service;

import com.fintracker.backend.auth_microservice.authentication.controller.dto.LoginDto;
import com.fintracker.backend.auth_microservice.authentication.controller.dto.SignUpDto;
import com.fintracker.backend.auth_microservice.authentication.controller.response.LoginResponse;
import com.fintracker.backend.auth_microservice.authentication.entity.User;
import com.fintracker.backend.auth_microservice.authentication.exception.custom.UserAlreadyExistsException;
import com.fintracker.backend.auth_microservice.authentication.repository.UserRepository;
import com.fintracker.backend.auth_microservice.authentication.security.service.JwtService;
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

    public User signup(SignUpDto dto) {
        if (userRepository.existsByUsernameOrEmail(dto.username(), dto.email())) {
            throw new UserAlreadyExistsException("Specified username or email is already occupied");
        }
        User user = User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .build();

        return userRepository.save(user);
    }

    public LoginResponse authenticate(LoginDto dto) {
        Optional<User> userOptional = userRepository.findUserByUsernameOrEmail(dto.usernameOrEmail());
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
                                    dto.password()
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
                                dto.usernameOrEmail(),
                                dto.password()
                        )
                )
        );
        return responseReference.get();
    }
}

