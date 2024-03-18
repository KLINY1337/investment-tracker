package com.fintracker.backend.fintrackermonolith.core.db.repository;

import com.fintracker.backend.fintrackermonolith.core.db.entity.TickerPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TickerPriceRepository extends JpaRepository<TickerPrice, Long> {
}