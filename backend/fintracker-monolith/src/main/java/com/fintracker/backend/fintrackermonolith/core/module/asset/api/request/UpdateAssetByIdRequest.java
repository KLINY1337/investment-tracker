package com.fintracker.backend.fintrackermonolith.core.module.asset.api.request;

public record UpdateAssetByIdRequest(
        String symbol,
        String assetTypeName
) {
}
