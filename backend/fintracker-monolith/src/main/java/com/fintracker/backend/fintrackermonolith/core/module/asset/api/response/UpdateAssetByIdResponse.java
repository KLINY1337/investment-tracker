package com.fintracker.backend.fintrackermonolith.core.module.asset.api.response;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Asset;

public record UpdateAssetByIdResponse(
        Boolean isSuccess,
        String message,
        Asset asset
) {
}
