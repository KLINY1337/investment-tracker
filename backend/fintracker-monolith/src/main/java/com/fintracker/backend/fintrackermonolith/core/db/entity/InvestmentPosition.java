package com.fintracker.backend.fintrackermonolith.core.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Setter
@Getter
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

    private Date openDate;
    private Date closeDate;

    private BigDecimal openQuoteAssetPrice;

    private BigDecimal closeQuoteAssetPrice;

    private BigDecimal baseAssetAmount;
}
