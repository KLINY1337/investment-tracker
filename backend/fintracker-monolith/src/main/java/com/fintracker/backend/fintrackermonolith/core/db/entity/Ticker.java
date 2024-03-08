package com.fintracker.backend.fintrackermonolith.core.db.entity;

import com.fintracker.backend.fintrackermonolith.core.db.enumeration.DenominationType;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.ExpirationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickers")
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

    private Boolean inUse = false;

    @ManyToOne
    @JoinColumn(name = "market_type_id")
    private MarketType marketType;
}
