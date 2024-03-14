package com.fintracker.backend.fintrackermonolith.core.db.repository;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Ticker;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.DenominationType;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.ExpirationType;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.MarketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TickerRepository extends JpaRepository<Ticker, Long> {
    boolean existsByExchangeTickerSymbolAndExchangeName(String exchangeTickerSymbol, String exchangeName);
    @Query("""
            select (count(t) > 0) from Ticker t
            where t.exchangeTickerSymbol = ?1 and t.exchangeName = ?2 and t.denominationType = ?3 and t.expirationType = ?4 and t.marketType = ?5""")
    boolean existsByExchangeTickerSymbolAndExchangeNameAndDenominationTypeAndExpirationTypeAndMarketType(String exchangeTickerSymbol, String exchangeName, DenominationType denominationType, ExpirationType expirationType, MarketType marketType);
}