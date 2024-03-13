package com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.response;

public record DeletePortfoliosByIdsResponse(
        Boolean isSuccess,
        String message,
        Integer deletedPortfoliosAmount
) {
}
