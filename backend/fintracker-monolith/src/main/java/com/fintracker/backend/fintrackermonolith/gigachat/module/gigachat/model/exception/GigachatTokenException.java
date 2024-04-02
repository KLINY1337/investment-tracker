package com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.model.exception;

public class GigachatTokenException extends Throwable {
    public GigachatTokenException(String noUnexpiredToken) {
        super(noUnexpiredToken);
    }
}
