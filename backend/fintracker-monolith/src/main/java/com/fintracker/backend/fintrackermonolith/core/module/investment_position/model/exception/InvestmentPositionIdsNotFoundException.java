package com.fintracker.backend.fintrackermonolith.core.module.investment_position.model.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InvestmentPositionIdsNotFoundException extends RuntimeException{

    private final List<Long> unprocessableIds;
    public InvestmentPositionIdsNotFoundException(String message, List<Long> unprocessableIds) {
        super(message);
        this.unprocessableIds = unprocessableIds;
    }
}
