package com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;

import java.util.List;

public record GetUsersResponse(
        Boolean isSuccess,
        String message,
        List<User> users
) {
}
