package com.fintracker.backend.fintrackermonolith.core.module.portfolio.model.exception;

public class PortfolioAlreadyExistsException extends RuntimeException{
    public PortfolioAlreadyExistsException(String message) {
        super(message);
    }
}
