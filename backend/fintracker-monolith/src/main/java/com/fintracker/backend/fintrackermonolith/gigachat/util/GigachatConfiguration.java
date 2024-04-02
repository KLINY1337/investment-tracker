package com.fintracker.backend.fintrackermonolith.gigachat.util;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import javax.net.ssl.SSLException;
import java.util.Collections;

@Configuration
@Slf4j
public class GigachatConfiguration {


    @Value("${gigachat.secret}")
    private String secretsBase64;

    @Bean
    WebClient setGigachatWebClient() throws SSLException
    {
        String authUrl = "https://ngw.devices.sberbank.ru:9443";
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();


        ClientHttpConnector httpConnector = new ReactorClientHttpConnector(HttpClient.create()
//                .wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                .secure(t -> t.sslContext(sslContext)));
        return WebClient.builder()
                .baseUrl(authUrl)
                .defaultHeaders(headers -> {
                    headers.setBasicAuth(secretsBase64);
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                })
                .clientConnector(httpConnector)
                .build();
    }
}
