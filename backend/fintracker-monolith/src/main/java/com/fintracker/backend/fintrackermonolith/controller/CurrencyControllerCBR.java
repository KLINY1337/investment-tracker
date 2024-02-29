package com.fintracker.backend.fintrackermonolith.controller;

import com.fintracker.backend.fintrackermonolith.controller.response.MainResponse;
import com.fintracker.backend.fintrackermonolith.exception.cbr.CBRRatesException;
import com.fintracker.backend.fintrackermonolith.service.cbr.CBRService;
import com.fintracker.backend.fintrackermonolith.service.cbr.dao.ValKursCBR;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
            return ResponseEntity.ok(response);
        }catch (CBRRatesException e){
            response = MainResponse.<ValKursCBR>builder().success(false).message("CBRRatesException").data(null).build();
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
