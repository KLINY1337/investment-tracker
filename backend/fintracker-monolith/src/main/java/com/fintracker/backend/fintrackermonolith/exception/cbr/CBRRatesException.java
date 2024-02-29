package com.fintracker.backend.fintrackermonolith.exception.cbr;

import lombok.Builder;

@Builder
public class CBRRatesException extends Exception{

    private String message;

    public CBRRatesException(String message) {
        super(message);
    }
}
