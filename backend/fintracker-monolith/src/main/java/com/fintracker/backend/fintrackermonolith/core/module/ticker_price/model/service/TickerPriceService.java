package com.fintracker.backend.fintrackermonolith.core.module.ticker_price.model.service;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Ticker;
import com.fintracker.backend.fintrackermonolith.core.db.entity.TickerPrice;
import com.fintracker.backend.fintrackermonolith.core.db.repository.TickerPriceRepository;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.model.service.TickerService;
import com.fintracker.backend.fintrackermonolith.core.module.ticker_price.api.response.GetTickerPriceByTickerIdAndMarkDateResponse;
import com.fintracker.backend.fintrackermonolith.core.module.ticker_price.model.exception.InvalidTickerPriceMarkDateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TickerPriceService {

    private final TickerPriceRepository tickerPriceRepository;
    private final TickerService tickerService;

    public GetTickerPriceByTickerIdAndMarkDateResponse getTickerPriceByTickerIdAndMarkDate(Long tickerId, Date markDate) {
        Ticker ticker = tickerService.getTickersByIds(List.of(tickerId)).tickers().get(0);
        if (markDate.after(new Date())) {
            throw new InvalidTickerPriceMarkDateException("Specified markDate is greater than current date");
        }

        markDate.setTime((markDate.getTime() / 1000 / 60 / 60) * 1000 * 60 * 60);
        TickerPrice tickerPrice = tickerPriceRepository.findByTickerAndMarkDate(ticker, markDate);

        return new GetTickerPriceByTickerIdAndMarkDateResponse(
                true,
                "Ticker price successfully retrieved from database",
                tickerPrice
        );
    }

    public List<TickerPrice> getTickerPriceByTicker(Ticker ticker) {
        return tickerPriceRepository.findByTicker(ticker);
    }
}
