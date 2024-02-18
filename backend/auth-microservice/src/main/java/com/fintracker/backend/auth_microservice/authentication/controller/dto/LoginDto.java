package com.fintracker.backend.auth_microservice.authentication.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record LoginDto (
        @NotNull(message = "Username or email must be not null")
        @NotEmpty(message = "Username  or email must be not empty")
        @NotBlank(message = "Username  or email must be not blank")
        String usernameOrEmail,

        @NotNull(message = "Password must be not null")
        @NotEmpty(message = "Password must be not empty")
        @NotBlank(message = "Password must be not blank")
        String password
) implements Serializable {
}
