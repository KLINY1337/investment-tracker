package com.fintracker.backend.fintrackermonolith.gigachat.util;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Ticker;
import com.fintracker.backend.fintrackermonolith.core.db.entity.TickerPrice;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.model.service.TickerService;
import com.fintracker.backend.fintrackermonolith.core.module.ticker_price.model.service.TickerPriceService;
import com.fintracker.backend.fintrackermonolith.gigachat.db.entity.GigachatToken;
import com.fintracker.backend.fintrackermonolith.gigachat.db.repositiory.GigachatTokenRepository;
import com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api.request.GigachatRequest;
import com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api.response.GigachatChoice;
import com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api.response.GigachatModelResponse;
import com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api.response.TickerAnalyzeResponse;
import com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.model.dto.GigachatMessage;
import com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.model.exception.GigachatTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class GigachatApiConnector {
    private final GigachatTokenRepository gigachatTokenRepository;
    private final WebClient gigachatWebClient;
    private final TickerService tickerService;
    private final TickerPriceService tickerPriceService;

    @Autowired
    GigachatApiConnector(GigachatTokenRepository gigachatTokenRepository, WebClient gigachatWebClient, TickerService tickerService, TickerPriceService tickerPriceService){
        this.gigachatTokenRepository = gigachatTokenRepository;
        this.gigachatWebClient = gigachatWebClient;
        this.tickerService = tickerService;
        this.tickerPriceService = tickerPriceService;
    }
    private final String baseUrl = "https://gigachat.devices.sberbank.ru/api/v1";


    public Mono<String> getToken() {
        String uuid = UUID.randomUUID().toString();
        return gigachatWebClient.post()
                .uri("https://ngw.devices.sberbank.ru:9443/api/v2/oauth")
                .header("RqUID", uuid)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .body(BodyInserters.fromFormData("scope","GIGACHAT_API_PERS"))
                .retrieve()
                .bodyToMono(GigachatToken.class)
                .publishOn(Schedulers.boundedElastic())
                .map(token -> {
                    gigachatTokenRepository.save(token);
                    return token.getAccess_token();
                });
    }

    public Mono<GigachatModelResponse> ping(String prompt) throws GigachatTokenException {
        GigachatRequest requestBody = gigachatInitialContextGenerator(prompt);

        return sendGenerationRequest(requestBody);
    }

    public Mono<GigachatModelResponse> analyzePriceGigachat(List<TickerPrice> tickerPriceList, Ticker ticker) throws GigachatTokenException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        tickerPriceList.forEach(tickerPrice -> {
            stringBuilder.append(tickerPrice.getMarkPrice()+" "+tickerPrice.getMarkDate()).append(",");
        });
        stringBuilder.append("]");

        String prompt = "Проанализируй цену актива. Я дам тебе данные о активе и массив его цен. Данные приведены далее" +
                "\n Данные о активе: Название " + ticker.getBaseAsset().getSymbol() + "/" + ticker.getQuoteAsset().getSymbol()+
                "\n Данные о цене актива в формате [price date,...,price date]: " + stringBuilder.toString();
        GigachatRequest requestBody = gigachatInitialContextGenerator(prompt);

        try {
            return sendGenerationRequest(requestBody);
        } catch (GigachatTokenException e) {
            e.printStackTrace();
            return Mono.error(e);
        }
    }

    public Mono<GigachatModelResponse> analyzePriceGigachat(int tickerId) throws GigachatTokenException {
        Ticker ticker = tickerService.getTickerById(tickerId);
        List<TickerPrice> tickerPriceList = tickerPriceService.getTickerPriceByTicker(ticker);
        return analyzePriceGigachat(tickerPriceList,ticker);
    }

    public TickerAnalyzeResponse analyzePrice(int tickerId) throws GigachatTokenException {
        Mono<GigachatModelResponse> response = analyzePriceGigachat(tickerId);
        GigachatModelResponse gigachatModelResponse = response.block();
        List<GigachatChoice> choices = gigachatModelResponse.getChoices();
        if (choices == null || choices.isEmpty()) {
            return new TickerAnalyzeResponse(false,"Failed to fetch messages from GigachatAPI response", "");
        }
        return new TickerAnalyzeResponse(true,"", choices.get(0).getMessage().getContent());
    }


    private GigachatRequest gigachatInitialContextGenerator(String prompt){

        GigachatMessage initialMessage = new GigachatMessage();
        initialMessage.setRole("system");
        initialMessage.setContent("Ты - Финансовый ассистент. Твоя задача - анализировать некоторые данные и давать свое объяснение.");

        GigachatMessage userMessage = new GigachatMessage();
        userMessage.setRole("user");
        userMessage.setContent(prompt);

        List<GigachatMessage> messages = new ArrayList<>();
        messages.add(initialMessage);
        messages.add(userMessage);

        GigachatRequest requestBody = new GigachatRequest();
        requestBody.setModel("GigaChat");
        requestBody.setMessages(messages);

        return requestBody;

    }

    private Mono<GigachatModelResponse> sendGenerationRequest(GigachatRequest requestBody) throws GigachatTokenException {
        GigachatToken tokenEntity = gigachatTokenRepository.getLastValidToken();
        if(tokenEntity == null) {
            throw new GigachatTokenException("No unexpired token");
        }

        return gigachatWebClient.post()
                .uri("https://gigachat.devices.sberbank.ru/api/v1/chat/completions")
                .header("Authorization", "Bearer " + tokenEntity.getAccess_token())
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(GigachatModelResponse.class);
    }


}
