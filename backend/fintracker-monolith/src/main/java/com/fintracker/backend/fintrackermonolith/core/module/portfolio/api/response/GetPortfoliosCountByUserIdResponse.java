package com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.response;

public record GetPortfoliosCountByUserIdResponse(
        Boolean isSuccess,
        String message,
        Long portfoliosAmount
) {
}
