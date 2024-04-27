package com.fintracker.backend.fintrackermonolith.core.module.asset.api.response;

public record DeleteAssetsByIdsResponse(
        Boolean isSuccess,
        String message,
        Integer deletedAssetsAmount
) {
}
