package com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response;

import com.fintracker.backend.fintrackermonolith.core.db.enumeration.MarketType;

import java.math.BigDecimal;

public record GetInvestmentPositionsDistributionByUserIdResponse(
        BigDecimal spotDistribution,
        BigDecimal futuresDistribution
) {
}
