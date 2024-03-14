package com.fintracker.backend.fintrackermonolith.core.module.ticker.api.response;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Ticker;

public record CreateTickerResponse(
        Boolean isSuccess,
        String message,
        Ticker ticker
) {
}
