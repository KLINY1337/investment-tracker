package com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response;

import java.math.BigDecimal;

public record GetInvestmentPositionsTotalPriceByUserIdResponse(
        Boolean isSuccess,
        String message,
        BigDecimal userPortfoliosTotalPrice
) {
}
