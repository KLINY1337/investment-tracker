package com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response;

import com.fintracker.backend.fintrackermonolith.core.db.entity.InvestmentPosition;

import java.util.List;

public record GetInvestmentPositionsByPortfoliosIdsResponse(
        Boolean isSuccess,
        String message,
        List<InvestmentPosition> investmentPositions
) {
}
