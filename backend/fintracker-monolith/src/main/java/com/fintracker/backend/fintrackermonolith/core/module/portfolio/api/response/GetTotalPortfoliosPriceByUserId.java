package com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.response;

import java.math.BigDecimal;

public record GetTotalPortfoliosPriceByUserId(
    Boolean isSuccess,
    String message,
    BigDecimal totalPortfoliosPrice
)
{}
