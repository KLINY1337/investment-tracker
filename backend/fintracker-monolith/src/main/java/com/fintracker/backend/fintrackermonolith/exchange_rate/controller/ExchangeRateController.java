package com.fintracker.backend.fintrackermonolith.exchange_rate.controller;

import com.fintracker.backend.fintrackermonolith.exchange_rate.controller.response.MainResponse;
import com.fintracker.backend.fintrackermonolith.exchange_rate.exception.cbr.CBRRatesException;
import com.fintracker.backend.fintrackermonolith.exchange_rate.service.ExchangeRateService;
import com.fintracker.backend.fintrackermonolith.exchange_rate.service.dao.ValKursCBR;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController("/exchange-rate")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public ResponseEntity<MainResponse<ValKursCBR>> getCurrency() {
        MainResponse<ValKursCBR> response;
        try {
            ValKursCBR valKursCBR = exchangeRateService.getDailyCurrencyRate();
             response = MainResponse.<ValKursCBR>builder().data(valKursCBR).success(true).message("200").build();
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES))
                    .body(response);
        }catch (CBRRatesException e){
            response = MainResponse.<ValKursCBR>builder().success(false).message("CBRRatesException").data(null).build();
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
