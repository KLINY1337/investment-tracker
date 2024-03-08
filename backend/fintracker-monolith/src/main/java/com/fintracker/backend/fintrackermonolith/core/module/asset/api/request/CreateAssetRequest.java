package com.fintracker.backend.fintrackermonolith.core.module.asset.api.request;

public record CreateAssetRequest(
        String symbol,
        String assetTypeName
) {
}
