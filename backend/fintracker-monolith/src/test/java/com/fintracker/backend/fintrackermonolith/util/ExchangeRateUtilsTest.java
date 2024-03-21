package com.fintracker.backend.fintrackermonolith.util;

import com.fintracker.backend.fintrackermonolith.core.util.ExchangeRateUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateUtilsTest {

    private static final Date FIXED_DATE;

    static {
        try {
            FIXED_DATE = new SimpleDateFormat("yyyy-MM-dd").parse("2024-03-10");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testConvert() {
        BigDecimal amount = new BigDecimal("10");
        BigDecimal expectedResult = new BigDecimal("908.0667430");

        BigDecimal result = ExchangeRateUtils.convertCurrencies("USD", "rub", amount, FIXED_DATE);
        assertEquals(expectedResult, result);
    }

    @Test
    void testExchangeRate() {
        BigDecimal expectedRate = new BigDecimal("90.8066743");

        BigDecimal result = ExchangeRateUtils.currenciesExchangeRate("USD", "rub", FIXED_DATE);
        assertEquals(expectedRate, result);
    }

    @Test
    void wrongFromAssetExchangeRate() {
        assertThrows(RuntimeException.class, () -> {
            ExchangeRateUtils.currenciesExchangeRate("iamnotanasset", "rub", FIXED_DATE);});
    }

    @Test
    void wrongToAssetExchangeRate() {
        assertThrows(RuntimeException.class, () -> {
            ExchangeRateUtils.currenciesExchangeRate("usd", "iamnotanasset", FIXED_DATE);});
    }

    @Test
    void wrongDateExchangeRate() {
        assertThrows(RuntimeException.class, () -> {
            ExchangeRateUtils.currenciesExchangeRate("usd", "rub", new Date(1000));});
    }



//    @Test
//    void testConvertWithFailedAPIs() {

//    }

//    @Test
//    void testExchangeRateWithFailedAPIs() {
//
//    }
}