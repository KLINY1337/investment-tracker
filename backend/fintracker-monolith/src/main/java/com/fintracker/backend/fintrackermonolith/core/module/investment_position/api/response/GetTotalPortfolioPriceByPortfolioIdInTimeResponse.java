package com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public record GetTotalPortfolioPriceByPortfolioIdInTimeResponse(
        List<Date> dates,
        List<BigDecimal> portfolioPrices
) {
}
