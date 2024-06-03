package com.fintracker.backend.fintrackermonolith.core.module.ticker_price.api;

import com.fintracker.backend.fintrackermonolith.core.module.ticker_price.api.response.GetTickerPriceByTickerIdAndMarkDateResponse;
import com.fintracker.backend.fintrackermonolith.core.module.ticker_price.model.service.TickerPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/ticker_prices")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://5.23.48.222:8080")
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
