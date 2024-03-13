package com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;
import com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api.request.LoginRequest;
import com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api.request.SignUpRequest;
import com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api.response.LoginResponse;
import com.fintracker.backend.fintrackermonolith.auth_server.module.auth.model.service.AuthService;
import com.fintracker.backend.fintrackermonolith.gigachat.db.entity.GigachatToken;
import com.fintracker.backend.fintrackermonolith.gigachat.util.GigachatApiConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/gigachat")
@RequiredArgsConstructor
public class GigachatController {
    private final GigachatApiConnector gigachatApiConnector;

    @PostMapping("/token")
    public ResponseEntity<String> token(){
        return ResponseEntity.ok(gigachatApiConnector.getToken());
    }
}