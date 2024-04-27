package com.fintracker.backend.fintrackermonolith.core.module.asset.model.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class AssetIdsNotFoundException extends RuntimeException{

    private final List<Long> unprocessableIds;
    public AssetIdsNotFoundException(String message, List<Long> unprocessableIds) {
        super(message);
        this.unprocessableIds = unprocessableIds;
    }
}
