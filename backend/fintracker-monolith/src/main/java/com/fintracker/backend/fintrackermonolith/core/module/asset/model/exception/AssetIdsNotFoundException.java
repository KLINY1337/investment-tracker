package com.fintracker.backend.fintrackermonolith.core.module.asset.model.exception;

import java.util.List;

public class AssetIdsNotFoundException extends RuntimeException{

    private final List<Long> unprocessableIds;
    public AssetIdsNotFoundException(String message, List<Long> unprocessableIds) {
        super(message);
        this.unprocessableIds = unprocessableIds;
    }
}
