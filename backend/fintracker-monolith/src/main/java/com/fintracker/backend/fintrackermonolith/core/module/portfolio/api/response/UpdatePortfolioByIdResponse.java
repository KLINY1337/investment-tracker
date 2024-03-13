package com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.response;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Portfolio;

public record UpdatePortfolioByIdResponse(
        Boolean isSuccess,
        String message,
        Portfolio portfolio
) {
}
