package com.fintracker.backend.fintrackermonolith.core.db.repository;

import com.fintracker.backend.fintrackermonolith.core.db.entity.MarketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketTypeRepository extends JpaRepository<MarketType, Long> {
}