package com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.response;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Portfolio;

import java.util.List;

public record GetPortfoliosByIdsResponse(
        Boolean isSuccess,
        String message,
        List<Portfolio> portfolios
) {
}
