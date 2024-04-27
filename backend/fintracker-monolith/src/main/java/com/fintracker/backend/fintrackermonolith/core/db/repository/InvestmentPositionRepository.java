package com.fintracker.backend.fintrackermonolith.core.db.repository;

import com.fintracker.backend.fintrackermonolith.core.db.entity.InvestmentPosition;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Portfolio;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response.GetInvestmentPositionsTotalPriceByUserIdResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface InvestmentPositionRepository extends JpaRepository<InvestmentPosition, Long> {
    @Query("select i from InvestmentPosition i where i.portfolio.id = ?1 and i.openDate < ?2")
    List<InvestmentPosition> findByPortfolio_IdAndOpenDateBefore(Long id, Date openDate);
    List<InvestmentPosition> findByPortfolio_User_Id(Long id);

    @Query("select i from InvestmentPosition i where i.portfolio in (?1)")
    List<InvestmentPosition> findAllByPortfolios(List<Portfolio> portfolios);
}