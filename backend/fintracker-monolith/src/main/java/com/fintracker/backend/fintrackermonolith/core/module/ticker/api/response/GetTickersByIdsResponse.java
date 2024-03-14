package com.fintracker.backend.fintrackermonolith.core.module.ticker.api.response;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Ticker;

import java.util.List;

public record GetTickersByIdsResponse(
        Boolean isSuccess,
        String message,
        List<Ticker> tickers
) {
}
