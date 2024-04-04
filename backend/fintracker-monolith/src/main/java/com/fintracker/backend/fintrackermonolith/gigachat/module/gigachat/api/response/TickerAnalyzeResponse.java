package com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api.response;

public record TickerAnalyzeResponse(
        Boolean isSuccess,
        String message,
        String data
) {}
