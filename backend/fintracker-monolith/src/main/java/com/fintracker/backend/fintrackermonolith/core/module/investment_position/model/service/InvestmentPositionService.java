package com.fintracker.backend.fintrackermonolith.core.module.investment_position.model.service;

import com.fintracker.backend.fintrackermonolith.core.db.entity.InvestmentPosition;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Portfolio;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Ticker;
import com.fintracker.backend.fintrackermonolith.core.db.repository.InvestmentPositionRepository;
import com.fintracker.backend.fintrackermonolith.core.module.asset.model.exception.AssetIdsNotFoundException;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response.CreateInvestmentPositionResponse;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response.DeleteInvestmentPositionsByIdsResponse;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response.GetInvestmentPositionsResponse;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response.UpdateInvestmentPositionByIdResponse;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.model.exception.InvalidTimePeriodException;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.model.exception.InvestmentPositionIdsNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestmentPositionService {

    private final InvestmentPositionRepository investmentPositionRepository;
    private final TickerService tickerService;
    private final PortfolioService portfolioService;
    private final MarketTypeService marketTypeService;

    public CreateInvestmentPositionResponse createInvestmentPosition(
            Long tickerId,
            Long portfolioId,
            Date openDate,
            Date closeDate,
            BigDecimal openQuoteAssetPrice,
            BigDecimal closeQuoteAssetPrice,
            BigDecimal baseAssetAmount) {
        Ticker ticker = tickerService.getTickersByIds(List.of(tickerId)).get(0);
        Portfolio portfolio = portfolioService.getPortfoliosByIds(List.of(portfolioId)).get(0);
        if (closeDate.before(openDate)) {
            throw new InvalidTimePeriodException("Open date must be before close date");
        }
        InvestmentPosition investmentPosition = InvestmentPosition.builder()
                .ticker(ticker)
                .portfolio(portfolio)
                .openDate(openDate)
                .closeDate(closeDate)
                .openQuoteAssetPrice(openQuoteAssetPrice)
                .closeQuoteAssetPrice(closeQuoteAssetPrice)
                .baseAssetAmount(baseAssetAmount)
                .build();
        return new CreateInvestmentPositionResponse(
                true,
                "Investment position successfully created",
                investmentPositionRepository.save(investmentPosition)
        );
    }

    public GetInvestmentPositionsResponse getInvestmentPositionsByIds(List<Long> ids) {
        Map<Boolean, List<Long>> processableAndUnprocessableIds = ids.stream()
                .collect(Collectors.partitioningBy(investmentPositionRepository::existsById));

        if (!processableAndUnprocessableIds.get(false).isEmpty()) {
            throw new InvestmentPositionIdsNotFoundException("Specified idList contains values, that don't exist in database", processableAndUnprocessableIds.get(false));
        }

        return new GetInvestmentPositionsResponse(
                true,
                "Specified investment positions retrieved from database",
                investmentPositionRepository.findAllById(processableAndUnprocessableIds.get(true))
        );
    }

    public UpdateInvestmentPositionByIdResponse updateInvestmentPositionById(
            Long id,
            Long tickerId,
            Long portfolioId,
            Date openDate,
            Date closeDate,
            BigDecimal openQuoteAssetPrice,
            BigDecimal closeQuoteAssetPrice,
            BigDecimal baseAssetAmount) {
        Ticker ticker = tickerService.getTickersByIds(List.of(tickerId)).get(0);
        Portfolio portfolio = portfolioService.getPortfoliosByIds(List.of(portfolioId)).get(0);
        InvestmentPosition investmentPosition = investmentPositionRepository
                .findById(id)
                .orElseThrow(() -> new InvestmentPositionIdsNotFoundException("Specified investment position does not exist in database", List.of(id)));

        investmentPosition.setTicker(ticker);
        investmentPosition.setPortfolio(portfolio);
        investmentPosition.setOpenDate(openDate);
        investmentPosition.setCloseDate(closeDate);
        investmentPosition.setOpenQuoteAssetPrice(openQuoteAssetPrice);
        investmentPosition.setCloseQuoteAssetPrice(closeQuoteAssetPrice);
        investmentPosition.setBaseAssetAmount(baseAssetAmount);

        return new UpdateInvestmentPositionByIdResponse(
                true,
                "Investment position successfully updated",
                investmentPosition
        );
    }

    public DeleteInvestmentPositionsByIdsResponse deleteInvestmentPositionsByIds(List<Long> ids) {
        Map<Boolean, List<Long>> processableAndUnprocessableIds = ids.stream()
                .collect(Collectors.partitioningBy(investmentPositionRepository::existsById));

        if (!processableAndUnprocessableIds.get(false).isEmpty()) {
            throw new AssetIdsNotFoundException("Specified idList contains values, that don't exist in database", processableAndUnprocessableIds.get(false));
        }

        investmentPositionRepository.deleteAllById(processableAndUnprocessableIds.get(true));
        return new DeleteInvestmentPositionsByIdsResponse(
                true,
                "Specified assets deleted from database",
                processableAndUnprocessableIds.get(true).size()
        );
    }
}