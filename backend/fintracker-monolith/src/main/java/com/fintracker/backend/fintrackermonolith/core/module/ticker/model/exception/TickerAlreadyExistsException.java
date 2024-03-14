package com.fintracker.backend.fintrackermonolith.core.module.ticker.model.exception;

public class TickerAlreadyExistsException extends RuntimeException{
    public TickerAlreadyExistsException(String message) {
        super(message);
    }
}
