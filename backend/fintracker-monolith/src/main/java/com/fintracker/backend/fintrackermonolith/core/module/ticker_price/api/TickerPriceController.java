package com.fintracker.backend.fintrackermonolith.core.module.ticker_price.api;

import com.fintracker.backend.fintrackermonolith.core.module.ticker_price.api.response.GetTickerPriceByTickerIdAndMarkDateResponse;
import com.fintracker.backend.fintrackermonolith.core.module.ticker_price.model.service.TickerPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/ticker_prices")
@RequiredArgsConstructor
public class TickerPriceController {

    private final TickerPriceService tickerPriceService;

    @GetMapping
    public ResponseEntity<GetTickerPriceByTickerIdAndMarkDateResponse> getTickerPriceByTickerIdAndMarkDate(@RequestParam Long tickerId, @RequestParam Date markDate) {
        return ResponseEntity.ok(tickerPriceService.getTickerPriceByTickerIdAndMarkDate(
                tickerId,
                markDate
        ));
    }
}
