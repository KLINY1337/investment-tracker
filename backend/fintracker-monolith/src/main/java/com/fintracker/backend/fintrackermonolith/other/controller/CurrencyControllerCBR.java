package com.fintracker.backend.fintrackermonolith.other.controller;

import com.fintracker.backend.fintrackermonolith.other.controller.response.MainResponse;
import com.fintracker.backend.fintrackermonolith.other.exception.cbr.CBRRatesException;
import com.fintracker.backend.fintrackermonolith.other.service.cbr.CBRService;
import com.fintracker.backend.fintrackermonolith.other.service.cbr.dao.ValKursCBR;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController("/cbr")
@RequiredArgsConstructor
public class CurrencyControllerCBR {

    private CBRService cbrService;

    @Autowired
    public CurrencyControllerCBR(CBRService cbrService) {
        this.cbrService = cbrService;
    }

    @GetMapping("/currency")
    public ResponseEntity<MainResponse<ValKursCBR>> getCurrency() {
        MainResponse<ValKursCBR> response = null;
        try {
            ValKursCBR valKursCBR = cbrService.getDailyCurrencyRate();
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
