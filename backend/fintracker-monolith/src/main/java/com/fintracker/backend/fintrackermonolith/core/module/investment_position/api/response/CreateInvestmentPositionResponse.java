package com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response;

import com.fintracker.backend.fintrackermonolith.core.db.entity.InvestmentPosition;

public record CreateInvestmentPositionResponse(
        Boolean isSuccess,
        String message,
        InvestmentPosition investmentPosition
) {
}
