package com.fintracker.backend.fintrackermonolith.core.module.ticker.api.response;

public record DeleteTickersByIdsResponse(
        Boolean isSuccess,
        String message,
        Integer deletedTickersAmount
) {
}
