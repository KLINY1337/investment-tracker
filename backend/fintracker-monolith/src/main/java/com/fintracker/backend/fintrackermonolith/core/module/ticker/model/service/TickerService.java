package com.fintracker.backend.fintrackermonolith.core.module.ticker.model.service;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Asset;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Ticker;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.DenominationType;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.ExpirationType;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.MarketType;
import com.fintracker.backend.fintrackermonolith.core.db.repository.TickerRepository;
import com.fintracker.backend.fintrackermonolith.core.module.asset.model.service.AssetService;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.api.response.CreateTickerResponse;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.api.response.DeleteTickersByIdsResponse;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.api.response.GetTickersByIdsResponse;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.api.response.UpdateTickerByIdResponse;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.model.exception.TickerAlreadyExistsException;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.model.exception.TickerIdsNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TickerService {

    private final TickerRepository tickerRepository;

    private final AssetService assetService;

    public CreateTickerResponse createTicker(
            String symbol,
            String exchangeTickerSymbol,
            String exchangeName,
            Long baseAssetId,
            Long quoteAssetId,
            DenominationType denominationType,
            ExpirationType expirationType,
            MarketType marketType,
            Boolean inUse) {

        Asset baseAsset = assetService.getAssetsByIds(List.of(baseAssetId)).assets().get(0);
        Asset quoteAsset = assetService.getAssetsByIds(List.of(quoteAssetId)).assets().get(0);
        if (tickerRepository.existsByExchangeTickerSymbolAndExchangeNameAndDenominationTypeAndExpirationTypeAndMarketType(
                exchangeTickerSymbol,
                exchangeName,
                denominationType,
                expirationType,
                marketType
        )) {
            throw new TickerAlreadyExistsException("Specified ticker already exists");
        }

        Ticker ticker = Ticker.builder()
                .symbol(symbol)
                .exchangeTickerSymbol(exchangeTickerSymbol)
                .exchangeName(exchangeName)
                .baseAsset(baseAsset)
                .quoteAsset(quoteAsset)
                .denominationType(denominationType)
                .expirationType(expirationType)
                .marketType(marketType)
                .inUse(inUse)
                .build();

        return new CreateTickerResponse(
                true,
                "Ticker successfully saved to database",
                tickerRepository.save(ticker)
        );
    }

    public GetTickersByIdsResponse getTickersByIds(List<Long> ids) {
        Map<Boolean, List<Long>> processableAndUnprocessableIds = ids.stream()
                .collect(Collectors.partitioningBy(tickerRepository::existsById));

        if (!processableAndUnprocessableIds.get(false).isEmpty()) {
            throw new TickerIdsNotFoundException("Specified idList contains values, that don't exist in database", processableAndUnprocessableIds.get(false));
        }

        return new GetTickersByIdsResponse(
                true,
                "Specified tickers retrieved from database",
                tickerRepository.findAllById(processableAndUnprocessableIds.get(true))
        );
    }

    public UpdateTickerByIdResponse updateTickerById(
            Long id,
            String symbol,
            String exchangeTickerSymbol,
            String exchangeName,
            Long baseAssetId,
            Long quoteAssetId,
            DenominationType denominationType,
            ExpirationType expirationType,
            MarketType marketType,
            Boolean inUse) {
        Asset baseAsset = assetService.getAssetsByIds(List.of(baseAssetId)).assets().get(0);
        Asset quoteAsset = assetService.getAssetsByIds(List.of(quoteAssetId)).assets().get(0);
        if (tickerRepository.existsByExchangeTickerSymbolAndExchangeNameAndDenominationTypeAndExpirationTypeAndMarketType(
                exchangeTickerSymbol,
                exchangeName,
                denominationType,
                expirationType,
                marketType
        )) {
            throw new TickerAlreadyExistsException("Specified ticker already exists");
        }

        Ticker ticker = tickerRepository
                .findById(id)
                .orElseThrow(() -> new TickerIdsNotFoundException("Specified ticker does not exist in database", List.of(id)));

        ticker.setSymbol(symbol);
        ticker.setExchangeTickerSymbol(exchangeTickerSymbol);
        ticker.setExchangeName(exchangeName);
        ticker.setBaseAsset(baseAsset);
        ticker.setQuoteAsset(quoteAsset);
        ticker.setDenominationType(denominationType);
        ticker.setExpirationType(expirationType);
        ticker.setMarketType(marketType);
        ticker.setInUse(inUse);

        return new UpdateTickerByIdResponse(
                true,
                "Ticker successfully updated",
                ticker
        );
    }

    public DeleteTickersByIdsResponse deleteTickersByIds(List<Long> ids) {
        Map<Boolean, List<Long>> processableAndUnprocessableIds = ids.stream()
                .collect(Collectors.partitioningBy(tickerRepository::existsById));

        if (!processableAndUnprocessableIds.get(false).isEmpty()) {
            throw new TickerIdsNotFoundException("Specified idList contains values, that don't exist in database", processableAndUnprocessableIds.get(false));
        }

        tickerRepository.deleteAllById(processableAndUnprocessableIds.get(true));
        return new DeleteTickersByIdsResponse(
                true,
                "Specified tickers deleted from database",
                processableAndUnprocessableIds.get(true).size()
        );
    }

    public Ticker getTickerById(long tickerId) {
        return tickerRepository.findById(tickerId).orElseThrow(() -> new TickerIdsNotFoundException("Specified ticker does not exist in database", List.of(tickerId)));
    }
}
