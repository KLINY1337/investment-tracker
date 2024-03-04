package com.fintracker.backend.fintrackermonolith.auth.controller;

import com.fintracker.backend.fintrackermonolith.auth.controller.request.LoginRequest;
import com.fintracker.backend.fintrackermonolith.auth.controller.request.SignUpRequest;
import com.fintracker.backend.fintrackermonolith.auth.controller.response.LoginResponse;
import com.fintracker.backend.fintrackermonolith.user.entity.User;
import com.fintracker.backend.fintrackermonolith.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request.username(), request.email(), request.password()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest request) {
        LoginResponse response = authenticationService.authenticate(request.usernameOrEmail(), request.password());
        if (response.isSuccess()) { // Status code: 200
            return ResponseEntity.ok(response);
        }
        else { // Status code: 403
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).body(response);
        }
    }
}
