package com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;
import com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api.request.LoginRequest;
import com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api.request.SignUpRequest;
import com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api.response.LoginResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.auth.model.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.signup(
                request.username(),
                request.email(),
                request.password())
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(
                request.usernameOrEmail(),
                request.password())
        );
    }
}
