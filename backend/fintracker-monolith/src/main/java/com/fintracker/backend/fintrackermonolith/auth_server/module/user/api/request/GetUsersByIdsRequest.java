package com.fintracker.backend.fintrackermonolith.auth_server.module.user.api.request;

import java.util.List;

public record GetUsersByIdsRequest(
        List<Long> ids
) {
}
