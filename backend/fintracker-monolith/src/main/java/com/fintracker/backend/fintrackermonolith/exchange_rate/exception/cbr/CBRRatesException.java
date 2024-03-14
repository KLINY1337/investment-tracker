package com.fintracker.backend.fintrackermonolith.exchange_rate.exception.cbr;

import lombok.Builder;

@Builder
public class CBRRatesException extends RuntimeException{

    private String message;

    public CBRRatesException(String message) {
        super(message);
    }
}
