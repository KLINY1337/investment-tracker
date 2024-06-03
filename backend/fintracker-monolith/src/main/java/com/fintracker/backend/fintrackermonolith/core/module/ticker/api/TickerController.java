package com.fintracker.backend.fintrackermonolith.core.module.ticker.api;

import com.fintracker.backend.fintrackermonolith.core.db.entity.Ticker;
import com.fintracker.backend.fintrackermonolith.core.db.repository.TickerRepository;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.api.request.CreateTickerRequest;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.api.request.UpdateTickerByIdRequest;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.api.response.CreateTickerResponse;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.api.response.DeleteTickersByIdsResponse;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.api.response.GetTickersByIdsResponse;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.api.response.UpdateTickerByIdResponse;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.model.service.TickerService;
import com.fintracker.backend.fintrackermonolith.core.util.RequestParamUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://5.23.48.222:8080")
public class TickerController {

    private final TickerService tickerService;
    private final TickerRepository tickerRepository;

    @PostMapping
    public ResponseEntity<CreateTickerResponse> createTicker(@RequestBody CreateTickerRequest request) {
        return ResponseEntity.ok(tickerService.createTicker(
                request.symbol(),
                request.exchangeTickerSymbol(),
                request.exchangeName(),
                request.baseAssetId(),
                request.quoteAssetId(),
                request.denominationType(),
                request.expirationType(),
                request.marketType(),
                request.inUse()
        ));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Ticker>> getAllTickers() {
        return ResponseEntity.ok(tickerRepository.findAll());
    }

    @GetMapping
    public ResponseEntity<GetTickersByIdsResponse> getTickersByIds(@RequestParam String idList) {
        return ResponseEntity.ok(tickerService.getTickersByIds(
                RequestParamUtils.parseParamAsList(idList, ",", Long::valueOf)
        ));
    }

    @PutMapping
    public ResponseEntity<UpdateTickerByIdResponse> updateTickerById(@RequestParam Long id, @RequestBody UpdateTickerByIdRequest request) {
        return ResponseEntity.ok(tickerService.updateTickerById(
                id,
                request.symbol(),
                request.exchangeTickerSymbol(),
                request.exchangeName(),
                request.baseAssetId(),
                request.quoteAssetId(),
                request.denominationType(),
                request.expirationType(),
                request.marketType(),
                request.inUse()
        ));
    }

    @DeleteMapping
    public ResponseEntity<DeleteTickersByIdsResponse> deleteTickersByIds(@RequestParam String idList) {
        return ResponseEntity.ok(tickerService.deleteTickersByIds(
                RequestParamUtils.parseParamAsList(idList, ",", Long::valueOf)
        ));
    }

}
