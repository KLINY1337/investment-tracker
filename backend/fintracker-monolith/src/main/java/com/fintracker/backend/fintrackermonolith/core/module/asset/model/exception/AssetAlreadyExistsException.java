package com.fintracker.backend.fintrackermonolith.core.module.asset.model.exception;

public class AssetAlreadyExistsException extends RuntimeException{
    public AssetAlreadyExistsException(String message) {
        super(message);
    }
}
