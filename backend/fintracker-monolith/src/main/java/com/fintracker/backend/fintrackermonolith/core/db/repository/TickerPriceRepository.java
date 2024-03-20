package com.fintracker.backend.fintrackermonolith.core.db.repository;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Ticker;
import com.fintracker.backend.fintrackermonolith.core.db.entity.TickerPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TickerPriceRepository extends JpaRepository<TickerPrice, Long> {
    @Query("select t from TickerPrice t where t.ticker = ?1 and t.markDate = ?2")
    TickerPrice findByTickerAndMarkDate(Ticker ticker, Date markDate);
}