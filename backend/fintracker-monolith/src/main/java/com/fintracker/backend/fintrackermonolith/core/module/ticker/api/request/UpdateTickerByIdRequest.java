package com.fintracker.backend.fintrackermonolith.core.module.ticker.api.request;

import com.fintracker.backend.fintrackermonolith.core.db.enumeration.DenominationType;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.ExpirationType;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.MarketType;

public record UpdateTickerByIdRequest(
        String symbol,
        String exchangeTickerSymbol,
        String exchangeName,
        Long baseAssetId,
        Long quoteAssetId,
        DenominationType denominationType,
        ExpirationType expirationType,
        MarketType marketType,
        Boolean inUse
) {
}
