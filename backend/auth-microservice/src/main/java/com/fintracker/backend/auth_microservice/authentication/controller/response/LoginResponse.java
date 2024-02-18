package com.fintracker.backend.auth_microservice.authentication.controller.response;


public record LoginResponse(
        Boolean success,
        String message,
        String token,
        Long expiresIn
) {
}
