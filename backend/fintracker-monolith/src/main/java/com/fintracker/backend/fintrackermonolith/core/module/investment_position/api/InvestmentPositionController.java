package com.fintracker.backend.fintrackermonolith.core.module.investment_position.api;

import com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.request.CreateInvestmentPositionRequest;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.request.UpdateInvestmentPositionByIdRequest;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response.*;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.model.service.InvestmentPositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/investment_positions")
@RequiredArgsConstructor
public class InvestmentPositionController {

    private final InvestmentPositionService investmentPositionService;

    @PostMapping
    public ResponseEntity<CreateInvestmentPositionResponse> createInvestmentPosition(@Valid @RequestBody CreateInvestmentPositionRequest request) {
        return ResponseEntity.ok(investmentPositionService.createInvestmentPosition(
                request.tickerId(),
                request.portfolioId(),
                request.openDate(),
                request.closeDate(),
                request.openQuoteAssetPrice(),
                request.closeQuoteAssetPrice(),
                request.baseAssetAmount()
        ));
    }

    @GetMapping("/byIds")
    public ResponseEntity<GetInvestmentPositionsResponse> getInvestmentPositionsByIds(@RequestParam List<Long> idList) {
        return ResponseEntity.ok(investmentPositionService.getInvestmentPositionsByIds(idList));
    }

    @PutMapping
    public ResponseEntity<UpdateInvestmentPositionByIdResponse> updateInvestmentPositionById(@RequestParam Long id, @RequestBody UpdateInvestmentPositionByIdRequest request) {
        return ResponseEntity.ok(investmentPositionService.updateInvestmentPositionById(
                id,
                request.tickerId(),
                request.portfolioId(),
                request.openDate(),
                request.closeDate(),
                request.openPriceUSD(),
                request.closePriceUSD(),
                request.baseAssetAmount()
        ));
    }

    @DeleteMapping
    public ResponseEntity<DeleteInvestmentPositionsByIdsResponse> deleteInvestmentPositionsByIds(@RequestParam List<Long> idList) {
        return ResponseEntity.ok(investmentPositionService.deleteInvestmentPositionsByIds(idList));
    }

    @GetMapping("/byPortfoliosIds")
    public ResponseEntity<GetInvestmentPositionsByPortfoliosIdsResponse> getInvestmentPositionsByPortfolios(@RequestParam List<Long> idList) {
        return ResponseEntity.ok(investmentPositionService.getInvestmentPositionsByPortfoliosIds(idList));
    }

    @GetMapping("/price")
    public ResponseEntity<GetInvestmentPositionPriceByIdResponse> getInvestmentPositionPriceById(@RequestParam Long investmentPositionId, @RequestParam Long quoteAssetId) {
        return ResponseEntity.ok(investmentPositionService.getInvestmentPositionPriceById(investmentPositionId, quoteAssetId));
    }
}
