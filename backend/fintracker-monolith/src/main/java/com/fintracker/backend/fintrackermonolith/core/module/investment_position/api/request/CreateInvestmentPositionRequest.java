package com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Date;

public record CreateInvestmentPositionRequest(
        Long tickerId,
        Long portfolioId,
        @NotNull @PastOrPresent Date openDate,
        @PastOrPresent Date closeDate,
        @NotNull @Positive BigDecimal openQuoteAssetPrice,
        @PositiveOrZero BigDecimal closeQuoteAssetPrice,
        @NotNull @Positive BigDecimal baseAssetAmount
) {
}
