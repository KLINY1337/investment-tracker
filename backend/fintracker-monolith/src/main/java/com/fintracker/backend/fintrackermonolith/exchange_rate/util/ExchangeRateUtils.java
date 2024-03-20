package com.fintracker.backend.fintrackermonolith.exchange_rate.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class ExchangeRateUtils {
    private final String BASE = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@";
    private final String FALLBACK = ".currency-api.pages.dev/";


    public BigDecimal convertCurrencies(String from, String to, BigDecimal amount, Date date){
        try {
            // Try to get the exchange rate from the primary API
            BigDecimal exchangeRate = currenciesExchangeRate(from, to, date, false);
            return amount.multiply(exchangeRate);
        } catch (RuntimeException e) {
            // If the primary API fails, try the fallback API
            try {
                BigDecimal exchangeRate = currenciesExchangeRate(from, to, date, true);
                return amount.multiply(exchangeRate);
            } catch (RuntimeException ex) {
                // If both APIs fail, throw an exception
                throw new RuntimeException("Failed to convert from " + from + " to " + to, ex);
            }
        }
    }

    public BigDecimal currenciesExchangeRate(String from, String to, Date date) {
        try {
            // Try to get the exchange rate from the primary API
            return currenciesExchangeRate(from, to, date, false);
        } catch (RuntimeException e) {
            // If the primary API fails, try the fallback API
            try {
                return currenciesExchangeRate(from, to, date, true);
            } catch (RuntimeException ex) {
                // If both APIs fail, throw an exception
                throw new RuntimeException("Failed to get exchange rate from " + from + " to " + to, ex);
            }
        }
    }


    private BigDecimal currenciesExchangeRate(String from, String to, Date date, boolean fallback) throws RuntimeException {
        String url = buildUrl(from, date, fallback);
        HttpURLConnection connection = null;

        try {
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }
                return parseExchangeRateResponse(from, to, response.toString());
            } else {
                throw new RuntimeException("Failed to retrieve exchange rate. Response code: " + responseCode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private BigDecimal parseExchangeRateResponse(String from, String to, String response) {
        JsonObject json = new Gson().fromJson(response, JsonObject.class);
        JsonObject fromObject = json.get(from.toLowerCase()).getAsJsonObject();
        return new BigDecimal(fromObject.get(to.toLowerCase()).getAsString());
    }

    private String buildUrl (String from, Date date, boolean fallback) {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        StringBuilder stringBuilder = new StringBuilder();
        if(!fallback){
            stringBuilder.append(BASE);
            stringBuilder.append(formattedDate);
            stringBuilder.append("/v1");
        }
        else{
            stringBuilder.append("https://");
            stringBuilder.append(formattedDate);
            stringBuilder.append(FALLBACK);
        }
        stringBuilder.append("/currencies/").append(from.toLowerCase()).append(".json");

        return stringBuilder.toString();
    }
}


