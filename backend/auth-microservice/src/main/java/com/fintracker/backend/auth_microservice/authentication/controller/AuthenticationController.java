package com.fintracker.backend.auth_microservice.authentication.controller;

import com.fintracker.backend.auth_microservice.authentication.controller.dto.LoginDto;
import com.fintracker.backend.auth_microservice.authentication.controller.dto.SignUpDto;
import com.fintracker.backend.auth_microservice.authentication.controller.response.LoginResponse;
import com.fintracker.backend.auth_microservice.authentication.entity.User;
import com.fintracker.backend.auth_microservice.authentication.security.service.JwtService;
import com.fintracker.backend.auth_microservice.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody SignUpDto signUpDto) {
        return ResponseEntity.ok(authenticationService.signup(signUpDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authenticationService.authenticate(loginDto));
    }
}

