package com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.response;

public record DeleteUsersByIdsResponse(
        Boolean isSuccess,
        String message,
        Integer deletedUsersAmount
) {
}
