package com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.request;

import jakarta.validation.constraints.Pattern;

public record CreateUserRequest(
        String username,
        String email,

        @Pattern(regexp = "^.*(?=.{8,})(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!#$%&? \"]).*$")
        String password
) {
}
