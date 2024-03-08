package com.fintracker.backend.fintrackermonolith.core.module.asset.api.response;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Asset;

import java.util.List;

public record GetAssetsResponse(
        Boolean isSuccess,
        String message,
        List<Asset> assets
) {
}
