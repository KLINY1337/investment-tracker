package com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.request;

public record CreatePortfolioRequest(
        Long userId,
        String name
) {
}
