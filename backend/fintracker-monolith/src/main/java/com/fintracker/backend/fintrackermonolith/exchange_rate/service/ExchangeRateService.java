package com.fintracker.backend.fintrackermonolith.exchange_rate.service;

import com.fintracker.backend.fintrackermonolith.exchange_rate.exception.cbr.CBRRatesException;
import com.fintracker.backend.fintrackermonolith.exchange_rate.service.dao.ValKursCBR;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;

@Service
@Slf4j
public class ExchangeRateService {

    @Cacheable("dailyRatesCBR")
    public ValKursCBR getDailyCurrencyRate() throws CBRRatesException {
        String url = "http://www.cbr.ru/scripts/XML_daily.asp";
        try {
            String xml = fetchXML(url);
            JAXBContext jaxbContext = JAXBContext.newInstance(ValKursCBR.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ValKursCBR valCurs = (ValKursCBR) jaxbUnmarshaller.unmarshal(new StringReader(xml));
            return valCurs;

        } catch (Exception e) {
            log.error("CBR rates excepton",e);
            e.printStackTrace();
            throw CBRRatesException.builder().message(e.getMessage()).build();
        }
    }


    public static String fetchXML(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }

    }


    @CacheEvict(value = "dailyRatesCBR", allEntries = true)
    @Scheduled(cron = "0 0 * * * *")
    public void emptyHotelsCache() {
        log.info("Emptying CBR daily rates Cache");
    }
}
