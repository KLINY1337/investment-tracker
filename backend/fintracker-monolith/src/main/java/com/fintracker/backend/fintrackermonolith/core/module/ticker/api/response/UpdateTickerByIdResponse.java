package com.fintracker.backend.fintrackermonolith.core.module.ticker.api.response;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Ticker;

public record UpdateTickerByIdResponse(
        Boolean isSuccess,
        String message,
        Ticker ticker
) {
}
