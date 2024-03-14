package com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO for {@link com.fintracker.backend.fintrackermonolith.core.db.entity.InvestmentPosition}
 */
public record InvestmentPositionDto(
        Long tickerId,
        Long portfolioId,
        Long marketTypeId,
        @NotNull @PastOrPresent Date openDate,
        @PastOrPresent Date closeDate,
        @NotNull @Positive BigDecimal openPriceUSD,
        @NotNull @PositiveOrZero BigDecimal closePriceUSD,
        @NotNull @Positive BigDecimal baseAssetAmount) {
}