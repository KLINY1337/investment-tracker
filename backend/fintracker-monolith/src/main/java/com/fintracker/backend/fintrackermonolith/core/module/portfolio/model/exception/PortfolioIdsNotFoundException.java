package com.fintracker.backend.fintrackermonolith.core.module.portfolio.model.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class PortfolioIdsNotFoundException extends RuntimeException{
    private final List<Long> unprocessableIds;
    public PortfolioIdsNotFoundException(String message, List<Long> unprocessableIds) {
        super(message);
        this.unprocessableIds = unprocessableIds;
    }
}
