package com.fintracker.backend.fintrackermonolith.gigachat.util;

import com.fintracker.backend.fintrackermonolith.gigachat.db.entity.GigachatToken;
import com.fintracker.backend.fintrackermonolith.gigachat.db.repositiory.GigachatTokenRepository;
import jakarta.annotation.PostConstruct;
import kong.unirest.core.MultipartBody;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GigachatApiConnector{

    private GigachatTokenRepository gigachatTokenRepository;
    private final String authUrl="https://ngw.devices.sberbank.ru:9443/api/v2/oauth";
    private final String baseUrl ="https://gigachat.devices.sberbank.ru/api/v1";

    @Value("${gigachat.secret}")
    private final String secretsBase64 = "NGYzMzMyMDUtMzZmMy00OTg2LWIwMzctMTg4MmVjZTM0NDRjOnNlY3JldA==";



    @PostConstruct
    public void init() {
        Unirest.config().verifySsl(false);
    }



    public String getToken() {
        String uuid = UUID.randomUUID().toString();

        GigachatToken gigachatTokenResponse = null;
        MultipartBody response = Unirest.post(authUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .header("RqUID", uuid)
                .header("Authorization", "Basic NGYzMzMyMDUtMzZmMy00OTg2LWIwMzctMTg4MmVjZTM0NDRjOjdjN2Q0ZTUxLTVhNDAtNDI1ZC1hMWQ4LTM4MWY4ZTVjNzljMQ==")
                .field("scope", "GIGACHAT_API_PERS");
        try {
            gigachatTokenResponse = response.asObject(GigachatToken.class).getBody();
            gigachatTokenRepository.save(gigachatTokenResponse);

        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

        return gigachatTokenResponse.getAccess_token();
    }
}
