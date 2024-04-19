package com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api.response;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;

public record LoginResponse(
        Boolean isSuccess,
        String message,
        String token,
        Long expiresIn,
        User user
) {
}
