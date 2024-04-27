package com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.request;

import java.math.BigDecimal;
import java.util.Date;

public record UpdateInvestmentPositionByIdRequest(
        Long tickerId,
        Long portfolioId,
        Date openDate,
        Date closeDate,
        BigDecimal openPriceUSD,
        BigDecimal openPriceRUR,
        BigDecimal closePriceUSD,
        BigDecimal closePriceRUR,
        BigDecimal baseAssetAmount
) {
}
