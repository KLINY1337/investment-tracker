package com.fintracker.backend.fintrackermonolith.gigachat.util;

import com.fintracker.backend.fintrackermonolith.gigachat.db.entity.GigachatToken;
import com.fintracker.backend.fintrackermonolith.gigachat.db.repositiory.GigachatTokenRepository;
import jakarta.annotation.PostConstruct;
import kong.unirest.core.MultipartBody;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v2/oauth")
                        .queryParam("scope", "GIGACHAT_API_PERS")
                        .build())
                .header("RqUID", uuid)
                .body(BodyInserters.empty())
                .retrieve()
                .bodyToMono(GigachatToken.class)
                .publishOn(Schedulers.boundedElastic())
                .map(token -> {
                    gigachatTokenRepository.save(token);
                    return token.getAccess_token();
                });
    }
}
