package com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.request;

public record UpdatePortfolioByIdRequest(
        Long userId,
        String name
) {
}
