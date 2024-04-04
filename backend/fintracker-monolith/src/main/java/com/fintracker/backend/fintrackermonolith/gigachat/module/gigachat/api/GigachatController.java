package com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api;

import com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api.response.GigachatModelResponse;
import com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api.response.TickerAnalyzeResponse;
import com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.model.exception.GigachatTokenException;
import com.fintracker.backend.fintrackermonolith.gigachat.util.GigachatApiConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/gigachat")
@RequiredArgsConstructor
public class GigachatController {
    private final GigachatApiConnector gigachatApiConnector;

    // todo appropriate error handling
    //todo refactor gigachat file hierarchy
    @PostMapping("/token")
    public ResponseEntity<Mono<String>> token(){
        System.out.println("123");
        return ResponseEntity.ok(gigachatApiConnector.getToken());
    }

    @PostMapping("/ping")
    public ResponseEntity<Mono<GigachatModelResponse>> ping(){
        System.out.println("generation");
        try{
            return ResponseEntity.ok(gigachatApiConnector.ping("Кто ты?"));
        } catch (GigachatTokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }


    @GetMapping("/admin/analyze")
    public ResponseEntity<Mono<GigachatModelResponse>> analyzeAdmin(@RequestParam int tickerId) throws GigachatTokenException {
        return ResponseEntity.ok(gigachatApiConnector.analyzePriceGigachat(tickerId));
    }

    @GetMapping("/analyze")
    public ResponseEntity<TickerAnalyzeResponse> analyze(@RequestParam int tickerId) throws GigachatTokenException {
        return ResponseEntity.ok(gigachatApiConnector.analyzePrice(tickerId));
    }


}