package com.fintracker.backend.fintrackermonolith.core.module.ticker_price.api;

import com.fintracker.backend.fintrackermonolith.core.module.ticker_price.model.service.TickerPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ticker_prices")
@RequiredArgsConstructor
public class TickerPriceController {

    private final TickerPriceService tickerPriceService;
}
