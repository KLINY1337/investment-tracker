package com.fintracker.backend.fintrackermonolith.core.db.entity;

import com.fintracker.backend.fintrackermonolith.core.db.enumeration.DenominationType;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.ExpirationType;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.MarketType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickers",
uniqueConstraints = @UniqueConstraint(
        columnNames = {"exchange_ticker_symbol", "exchange_name"})
)
public class Ticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private String exchangeTickerSymbol;
    private String exchangeName;

    @ManyToOne
    @JoinColumn(name = "base_asset_id")
    private Asset baseAsset;

    @ManyToOne
    @JoinColumn(name = "quote_asset_id")
    private Asset quoteAsset;

    @Enumerated(value = EnumType.STRING)
    private DenominationType denominationType;

    @Enumerated(value = EnumType.STRING)
    private ExpirationType expirationType;

    @Enumerated(value = EnumType.STRING)
    private MarketType marketType;

    private Boolean inUse;
}
