package com.fintracker.backend.fintrackermonolith.util;

import com.fintracker.backend.fintrackermonolith.exchange_rate.util.CurrencyConverter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyConverterTest {

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

        BigDecimal result = CurrencyConverter.convert("USD", "rub", amount, FIXED_DATE);
        assertEquals(expectedResult, result);
    }

    @Test
    void testExchangeRate() {
        BigDecimal expectedRate = new BigDecimal("90.8066743");

        BigDecimal result = CurrencyConverter.exchangeRate("USD", "rub", FIXED_DATE);
        assertEquals(expectedRate, result);
    }

    @Test
    void wrongFromAssetExchangeRate() {
        assertThrows(RuntimeException.class, () -> {CurrencyConverter.exchangeRate("iamnotanasset", "rub", FIXED_DATE);});
    }

    @Test
    void wrongToAssetExchangeRate() {
        assertThrows(RuntimeException.class, () -> {CurrencyConverter.exchangeRate("usd", "iamnotanasset", FIXED_DATE);});
    }

    @Test
    void wrongDateExchangeRate() {
        assertThrows(RuntimeException.class, () -> {CurrencyConverter.exchangeRate("usd", "rub", new Date(1000));});
    }



//    @Test
//    void testConvertWithFailedAPIs() {

//    }

//    @Test
//    void testExchangeRateWithFailedAPIs() {
//
//    }
}