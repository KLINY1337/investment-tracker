package com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;

public record CreateUserResponse(
        Boolean isSuccess,
        String message,
        User user
) {
}
