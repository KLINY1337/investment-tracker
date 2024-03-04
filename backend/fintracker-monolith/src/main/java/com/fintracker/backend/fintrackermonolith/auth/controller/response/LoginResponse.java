package com.fintracker.backend.fintrackermonolith.auth.controller.response;

public record LoginResponse(
        Boolean isSuccess,
        String message,
        String token,
        Long expiresIn
) {
}
