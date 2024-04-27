package com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class UserIdsNotFoundException extends RuntimeException {
    private final List<Long> unprocessableIds;
    public UserIdsNotFoundException(String message, List<Long> unprocessableIds) {
        super(message);
        this.unprocessableIds = unprocessableIds;
    }
}
