package com.fintracker.backend.fintrackermonolith.core.module.ticker.model.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class TickerIdsNotFoundException extends RuntimeException{

    private final List<Long> unprocessableIds;
    public TickerIdsNotFoundException(String message, List<Long> unprocessableIds) {
        super(message);
        this.unprocessableIds = unprocessableIds;
    }
}
