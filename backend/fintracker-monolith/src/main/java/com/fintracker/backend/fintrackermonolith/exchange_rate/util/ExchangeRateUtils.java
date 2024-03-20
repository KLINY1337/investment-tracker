package com.fintracker.backend.fintrackermonolith.exchange_rate.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;

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


    public BigDecimal convertCurrencies(String baseAssetName, String quoteAssetName, BigDecimal baseAssetAmount, Date quotationDate){
        try {
            // Try to get the exchange rate from the primary API
            BigDecimal exchangeRate = currenciesExchangeRateRequest(baseAssetName, quoteAssetName, quotationDate, false);
            return baseAssetAmount.multiply(exchangeRate);
        } catch (RuntimeException e) {
            // If the primary API fails, try the fallback API
            try {
                BigDecimal exchangeRate = currenciesExchangeRateRequest(baseAssetName, quoteAssetName, quotationDate, true);
                return baseAssetAmount.multiply(exchangeRate);
            } catch (RuntimeException ex) {
                // If both APIs fail, throw an exception
                throw new RuntimeException("Failed to convert from " + baseAssetName + " to " + quoteAssetName, ex);
            }
        }
    }

    public BigDecimal currenciesExchangeRate(String baseAssetName, String quoteAssetName, Date quotationDate) {
        try {
            // Try to get the exchange rate from the primary API
            return currenciesExchangeRateRequest(baseAssetName, quoteAssetName, quotationDate, false);
        } catch (RuntimeException e) {
            // If the primary API fails, try the fallback API
            try {
                return currenciesExchangeRateRequest(baseAssetName, quoteAssetName, quotationDate, true);
            } catch (RuntimeException ex) {
                // If both APIs fail, throw an exception
                throw new RuntimeException("Failed to get exchange rate from " + baseAssetName + " to " + quoteAssetName, ex);
            }
        }
    }


    private BigDecimal currenciesExchangeRateRequest(String baseAssetName, String quoteAssetName, Date quotationDate, boolean fallback) throws RuntimeException {
        String url = buildUrl(baseAssetName, quotationDate, fallback);
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
                return parseExchangeRateResponse(baseAssetName, quoteAssetName, response.toString());
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

    private BigDecimal parseExchangeRateResponse(String baseAssetName, String quoteAssetName, String apiResponse) {
        JsonObject json = new Gson().fromJson(apiResponse, JsonObject.class);
        JsonObject fromObject = json.get(baseAssetName.toLowerCase()).getAsJsonObject();
        return new BigDecimal(fromObject.get(quoteAssetName.toLowerCase()).getAsString());
    }

    private String buildUrl (String baseAssetName, Date quoteAssetName, boolean fallback) {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(quoteAssetName);
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
        stringBuilder.append("/currencies/").append(baseAssetName.toLowerCase()).append(".json");

        return stringBuilder.toString();
    }
}


