package com.fintracker.backend.fintrackermonolith.auth_server.module.auth.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record SignUpRequest (
        @NotNull(message = "Username must be not null")
        @NotEmpty(message = "Username must be not empty")
        @NotBlank(message = "Username must be not blank")
        String username,

        @NotNull(message = "Email must be not null")
        @Email(message = "Incorrect email")
        @NotEmpty(message = "Email must be not empty")
        @NotBlank(message = "Email must be not blank")
        String email,

        @NotNull(message = "Password must be not null")
        @NotEmpty(message = "Password must be not empty")
        @NotBlank(message = "Password must be not blank")
        String password) implements Serializable {

}
