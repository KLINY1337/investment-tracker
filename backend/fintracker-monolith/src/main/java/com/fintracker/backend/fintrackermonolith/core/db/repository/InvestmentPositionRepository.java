package com.fintracker.backend.fintrackermonolith.core.db.repository;

import com.fintracker.backend.fintrackermonolith.core.db.entity.InvestmentPosition;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentPositionRepository extends JpaRepository<InvestmentPosition, Long> {
    @Query("select i from InvestmentPosition i where i.portfolio in (?1)")
    List<InvestmentPosition> findAllByPortfolios(List<Portfolio> portfolios);
}