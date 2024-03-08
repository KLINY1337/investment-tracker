package com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api.response;

public record LoginResponse(
        Boolean isSuccess,
        String message,
        String token,
        Long expiresIn
) {
}
