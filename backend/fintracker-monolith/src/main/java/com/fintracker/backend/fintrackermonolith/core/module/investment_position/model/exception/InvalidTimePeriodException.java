package com.fintracker.backend.fintrackermonolith.core.module.investment_position.model.exception;

public class InvalidTimePeriodException extends RuntimeException{
    public InvalidTimePeriodException(String message) {
        super(message);
    }
}
