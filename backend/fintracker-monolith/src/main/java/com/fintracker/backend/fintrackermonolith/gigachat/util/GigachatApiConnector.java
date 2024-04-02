package com.fintracker.backend.fintrackermonolith.gigachat.util;

import com.fintracker.backend.fintrackermonolith.gigachat.db.entity.GigachatToken;
import com.fintracker.backend.fintrackermonolith.gigachat.db.repositiory.GigachatTokenRepository;
import com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api.request.GigachatRequest;
import com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api.response.GigachatModelResponse;
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

    @Autowired
    GigachatApiConnector(GigachatTokenRepository gigachatTokenRepository, WebClient gigachatWebClient){
        this.gigachatTokenRepository = gigachatTokenRepository;
        this.gigachatWebClient = gigachatWebClient;
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

    public Mono<GigachatModelResponse> getResponse(String prompt) throws GigachatTokenException {
        GigachatToken tokenEntity = gigachatTokenRepository.getLastValidToken();
        if(tokenEntity == null) {
            throw new GigachatTokenException("No unexpired token");
        }

        GigachatMessage initialMessage = new GigachatMessage();
        initialMessage.setRole("system");
        initialMessage.setContent("Ты - маг таро");

        GigachatMessage userMessage = new GigachatMessage();
        userMessage.setRole("user");
        userMessage.setContent(prompt);

        List<GigachatMessage> messages = new ArrayList<>();
        messages.add(initialMessage);
        messages.add(userMessage);


        GigachatRequest requestBody = new GigachatRequest();
        requestBody.setModel("GigaChat");
        requestBody.setMessages(messages);


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
