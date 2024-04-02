package com.fintracker.backend.fintrackermonolith.gigachat.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Configuration
public class GigachatConfiguration {

    @Value("${gigachat.secret}")
    private String secretsBase64;

    @Bean
    WebClient setGigachatWebClient(){
        String authUrl = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth";
        return WebClient.builder()
                .baseUrl(authUrl)
                .defaultHeaders(headers -> {
                    headers.setBasicAuth(secretsBase64);
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                })
                .build();
    }
}
