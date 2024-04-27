package com.fintracker.backend.fintrackermonolith.core.module.ticker_price.api.response;

import com.fintracker.backend.fintrackermonolith.core.db.entity.TickerPrice;

import java.math.BigDecimal;

public record GetTickerPriceByTickerIdAndMarkDateResponse(
        Boolean isSuccess,
        String message,
        TickerPrice tickerPrice
) {
}
