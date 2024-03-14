package com.fintracker.backend.fintrackermonolith.exchange_rate.service;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Asset;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Ticker;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.DenominationType;
import com.fintracker.backend.fintrackermonolith.core.db.enumeration.MarketType;
import com.fintracker.backend.fintrackermonolith.core.db.repository.AssetRepository;
import com.fintracker.backend.fintrackermonolith.core.db.repository.TickerRepository;
import com.fintracker.backend.fintrackermonolith.exchange_rate.exception.cbr.CBRRatesException;
import com.fintracker.backend.fintrackermonolith.exchange_rate.service.dao.ValKursCBR;
import com.fintracker.backend.fintrackermonolith.exchange_rate.service.dao.ValuteCBR;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateService {
    private final AssetRepository assetRepository;
    private final TickerRepository tickerRepository;

    @Cacheable("dailyRatesCBR")
    public ValKursCBR getDailyCurrencyRate() throws CBRRatesException {
        String url = "http://www.cbr.ru/scripts/XML_daily.asp";
        try {
            String xml = fetchXML(url);
            JAXBContext jaxbContext = JAXBContext.newInstance(ValKursCBR.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ValKursCBR valCurs = (ValKursCBR) jaxbUnmarshaller.unmarshal(new StringReader(xml));
            valCurs.getValutes().forEach(valute -> writeToDatabase(valute));
            return valCurs;

        } catch (Exception e) {
            log.error("CBR rates excepton",e);
            e.printStackTrace();
            throw CBRRatesException.builder().message(e.getMessage()).build();
        }
    }

    public boolean writeToDatabase (ValuteCBR valuteCBR) {
        if(!assetRepository.existsBySymbol(valuteCBR.getCharCode())){
            Asset baseAsset = new Asset();
            baseAsset.setAssetTypeName("FIAT");
            baseAsset.setSymbol(valuteCBR.getCharCode());
            assetRepository.save(baseAsset);
        }
        
        //optimize??
        if(!assetRepository.existsBySymbol("RUB")){
            Asset rubAsset = new Asset();
            rubAsset.setAssetTypeName("FIAT");
            rubAsset.setSymbol("RUB");
            assetRepository.save(rubAsset);
        }
        Ticker ticker = new Ticker();
        ticker.setBaseAsset(assetRepository.findBySymbol(valuteCBR.getCharCode()));
        ticker.setSymbol(valuteCBR.getName());
        ticker.setQuoteAsset(assetRepository.findBySymbol("RUB"));
        ticker.setMarketType(MarketType.SPOT);
        ticker.setInUse(true);
        ticker.setDenominationType(DenominationType.VANILLA);
        ticker.setExchangeName("ЦБ РФ");
        ticker.setExchangeTickerSymbol(valuteCBR.getCharCode());
        if(!tickerRepository.existsByExchangeTickerSymbolAndExchangeName(
                ticker.getExchangeTickerSymbol(),
                ticker.getExchangeName())){
            tickerRepository.save(ticker);
        } //todo: else update ticker
        return true;
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
