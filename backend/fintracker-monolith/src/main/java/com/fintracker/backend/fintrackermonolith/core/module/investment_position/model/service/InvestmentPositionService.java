package com.fintracker.backend.fintrackermonolith.core.module.investment_position.model.service;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.service.UserService;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Asset;
import com.fintracker.backend.fintrackermonolith.core.db.entity.InvestmentPosition;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Portfolio;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Ticker;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.MarketType;
import com.fintracker.backend.fintrackermonolith.core.db.repository.InvestmentPositionRepository;
import com.fintracker.backend.fintrackermonolith.core.db.repository.PortfolioRepository;
import com.fintracker.backend.fintrackermonolith.core.module.asset.model.exception.AssetIdsNotFoundException;
import com.fintracker.backend.fintrackermonolith.core.module.asset.model.service.AssetService;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response.*;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.model.exception.InvalidTimePeriodException;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.model.exception.InvestmentPositionIdsNotFoundException;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.model.service.PortfolioService;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.model.service.TickerService;
import com.fintracker.backend.fintrackermonolith.core.util.ExchangeRateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sound.sampled.Port;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestmentPositionService {

    private final InvestmentPositionRepository investmentPositionRepository;
    private final TickerService tickerService;
    private final PortfolioService portfolioService;
    private final AssetService assetService;
    private final UserService userService;
    private final PortfolioRepository portfolioRepository;

    public CreateInvestmentPositionResponse createInvestmentPosition(
            Long tickerId,
            Long portfolioId,
            Date openDate,
            Date closeDate,
            BigDecimal openQuoteAssetPrice,
            BigDecimal closeQuoteAssetPrice,
            BigDecimal baseAssetAmount) {
        Ticker ticker = tickerService.getTickersByIds(List.of(tickerId)).tickers().get(0);
        Portfolio portfolio = portfolioService.getPortfoliosByIds(List.of(portfolioId)).portfolios().get(0);
        if (closeDate != null && closeDate.before(openDate)) {
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

    public GetInvestmentPositionsDistributionByUserIdResponse getInvestmentPositionsDistributionByUserId(Long userId) {
        List<InvestmentPosition> investmentPositions = investmentPositionRepository.findByPortfolio_User_Id(userId);
        final BigDecimal[] spotPrice = {BigDecimal.ZERO};
        final BigDecimal[] futuresPrice = {BigDecimal.ZERO};
        investmentPositions.forEach(investmentPosition -> {
            switch (investmentPosition.getTicker().getMarketType()) {
                case FUTURE -> futuresPrice[0] = futuresPrice[0].add(investmentPosition.getCloseQuoteAssetPrice().multiply(investmentPosition.getBaseAssetAmount()));
                case SPOT -> spotPrice[0] = spotPrice[0].add(ExchangeRateUtils.convertCurrencies(investmentPosition.getTicker().getBaseAsset().getSymbol(), investmentPosition.getTicker().getQuoteAsset().getSymbol(), investmentPosition.getBaseAssetAmount(), new Date()));
            }
        });

        BigDecimal fullPrice = spotPrice[0].add(futuresPrice[0]);
        return new GetInvestmentPositionsDistributionByUserIdResponse(
                spotPrice[0].divide(fullPrice).multiply(new BigDecimal(100)),
                futuresPrice[0].divide(fullPrice).multiply(new BigDecimal(100))
        );
    }

    public GetInvestmentPositionsTotalPriceByUserIdResponse getInvestmentPositionsTotalPriceByUserId(Long userId) {
        List<InvestmentPosition> investmentPositions = investmentPositionRepository.findByPortfolio_User_Id(userId);
        final BigDecimal[] totalInvestmentPositionsPrice = {BigDecimal.ZERO};
        investmentPositions.forEach(investmentPosition -> {
            switch (investmentPosition.getTicker().getMarketType()) {
                case FUTURE -> totalInvestmentPositionsPrice[0] = totalInvestmentPositionsPrice[0].add(investmentPosition.getCloseQuoteAssetPrice().multiply(investmentPosition.getBaseAssetAmount()));
                case SPOT -> totalInvestmentPositionsPrice[0] = totalInvestmentPositionsPrice[0].add(ExchangeRateUtils.convertCurrencies(investmentPosition.getTicker().getBaseAsset().getSymbol(), investmentPosition.getTicker().getQuoteAsset().getSymbol(), investmentPosition.getBaseAssetAmount(), new Date()));
            }
        });
        return new GetInvestmentPositionsTotalPriceByUserIdResponse(
                true,
                "Investment positions price poshitani",
                totalInvestmentPositionsPrice[0]
        );
    }

    public GetProfitByPortfolioIdResponse getProfitByPortfolioId(Long portfolioId) {
        List<InvestmentPosition> investmentPositions = getInvestmentPositionsByPortfoliosIds(List.of(portfolioId)).investmentPositions();
        final BigDecimal[] initialPrice = {BigDecimal.ZERO};
        investmentPositions.forEach(investmentPosition -> {
            initialPrice[0] = initialPrice[0].add(investmentPosition.getBaseAssetAmount().multiply(investmentPosition.getOpenQuoteAssetPrice()));
        });

        final BigDecimal[] currentPrice = {BigDecimal.ZERO};
        investmentPositions.forEach(investmentPosition -> {
            switch (investmentPosition.getTicker().getMarketType()) {
                case FUTURE -> currentPrice[0] = currentPrice[0].add(investmentPosition.getCloseQuoteAssetPrice().multiply(investmentPosition.getBaseAssetAmount()));
                case SPOT -> currentPrice[0] = currentPrice[0].add(ExchangeRateUtils.convertCurrencies(investmentPosition.getTicker().getBaseAsset().getSymbol(), investmentPosition.getTicker().getQuoteAsset().getSymbol(), investmentPosition.getBaseAssetAmount(), new Date()));
            }
        });

        return new GetProfitByPortfolioIdResponse(
                true,
                "Poshitali profit",
                currentPrice[0].divide(initialPrice[0]).subtract(BigDecimal.ONE).multiply(new BigDecimal(100))
        );
    }

    public GetTotalPortfolioPriceByPortfolioIdInTimeResponse getTotalPortfolioPriceByPortfolioIdAndDate(Long portfolioId, Date date) {
        List<Date> dates = new ArrayList<>();
        List<BigDecimal> prices = new ArrayList<>();
        investmentPositionRepository.findByPortfolio_IdAndOpenDateBefore(portfolioId, date).forEach(investmentPosition -> {
            dates.add(investmentPosition.getOpenDate());
            prices.add(investmentPosition.getOpenQuoteAssetPrice().multiply(investmentPosition.getBaseAssetAmount()));
        });
        return new GetTotalPortfolioPriceByPortfolioIdInTimeResponse(dates, prices);
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
        Ticker ticker = tickerService.getTickersByIds(List.of(tickerId)).tickers().get(0);
        Portfolio portfolio = portfolioService.getPortfoliosByIds(List.of(portfolioId)).portfolios().get(0);
        if (closeDate != null && closeDate.before(openDate)) {
            throw new InvalidTimePeriodException("Open date must be before close date");
        }
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

    public GetInvestmentPositionsByPortfoliosIdsResponse getInvestmentPositionsByPortfoliosIds(List<Long> ids) {
        List<Portfolio> portfolios = portfolioService.getPortfoliosByIds(ids).portfolios();
        return new GetInvestmentPositionsByPortfoliosIdsResponse(
                true,
                "Investment positions successfully retrieved from database",
                investmentPositionRepository.findAllByPortfolios(portfolios)
        );
    }

    public GetInvestmentPositionPriceByIdResponse getInvestmentPositionPriceById(Long investmentPositionId,
                                                                                 Long quoteAssetId,
                                                                                 Date quotationDate) {
        InvestmentPosition investmentPosition = getInvestmentPositionsByIds(List.of(investmentPositionId)).investmentPositions().get(0);
        Asset baseAsset = investmentPosition.getTicker().getBaseAsset();
        Asset quoteAsset = assetService.getAssetsByIds(List.of(quoteAssetId)).assets().get(0);
        return new GetInvestmentPositionPriceByIdResponse(
                true,
                "Investment position price successfully calculated",
                ExchangeRateUtils.convertCurrencies(
                        baseAsset.getSymbol(),
                        quoteAsset.getSymbol(),
                        investmentPosition.getBaseAssetAmount(),
                        quotationDate
                )
        );
    }

    public GetInvestmentPositionsTotalPriceByPortfolioIdResponse getInvestmentPositionsTotalPriceByPortfolioId(Long portfolioId,
                                                                                                               Long quoteAssetId,
                                                                                                               Date quotationDate) {
        Portfolio portfolio = portfolioService.getPortfoliosByIds(List.of(portfolioId)).portfolios().get(0);
        List<InvestmentPosition> investmentPositions = investmentPositionRepository.findAllByPortfolios(List.of(portfolio));

        Optional<BigDecimal> portfolioPriceOptional = investmentPositions.parallelStream()
                .map(investmentPosition -> getInvestmentPositionPriceById(investmentPosition.getId(), quoteAssetId, quotationDate).investmentPositionPrice())
                .reduce(BigDecimal::add);

        return new GetInvestmentPositionsTotalPriceByPortfolioIdResponse(
                true,
                "Portfolio price successfully calculated",
                portfolioPriceOptional.orElse(BigDecimal.ZERO)
        );
    }
}
