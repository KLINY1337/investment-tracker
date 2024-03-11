package com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response;

public record DeleteInvestmentPositionsByIdsResponse(
        Boolean isSuccess,
        String message,
        Integer deletedInvestmentPositionsAmount
) {
}
