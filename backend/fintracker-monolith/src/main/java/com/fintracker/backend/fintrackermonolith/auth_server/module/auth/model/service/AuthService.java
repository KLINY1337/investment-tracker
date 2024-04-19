package com.fintracker.backend.fintrackermonolith.auth_server.module.auth.model.service;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;
import com.fintracker.backend.fintrackermonolith.auth_server.db.repository.UserRepository;
import com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api.response.LoginResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.service.UserService;
import com.fintracker.backend.fintrackermonolith.auth_server.util.AccessTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public User signup(String username, String email, String password) {
        return userService
                .createUser(username, email, password)
                .user();
    }

    public LoginResponse authenticate(String usernameOrEmail, String password) {
        User user = userService.getUserByUsernameOrEmail(usernameOrEmail);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        password
                )
        );

        return new LoginResponse(
                true,
                "User successfully authenticated",
                AccessTokenUtils.generateToken(user),
                AccessTokenUtils.getExpirationTime(),
                user
        );
    }
}
