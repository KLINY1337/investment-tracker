package com.fintracker.backend.fintrackermonolith.core.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "investment_positions")
public class InvestmentPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticker_id")
    private Ticker ticker;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @ManyToOne
    @JoinColumn(name = "market_type_id")
    private MarketType marketType;

    private Date openDate;
    private Date closeDate;

    private BigDecimal openPriceUSD;
    private BigDecimal openPriceRUR;

    private BigDecimal closePriceUSD;
    private BigDecimal closePriceRUR;
}