package com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;

public record UpdateUserByIdResponse(
        Boolean isSuccess,
        String message,
        User updatedUser
) {
}
