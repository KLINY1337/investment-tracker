package com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response;

import java.math.BigDecimal;

public record GetProfitByPortfolioIdResponse(
        boolean isSuccess,
        String message,
        BigDecimal portfolioProfit
) {
}
