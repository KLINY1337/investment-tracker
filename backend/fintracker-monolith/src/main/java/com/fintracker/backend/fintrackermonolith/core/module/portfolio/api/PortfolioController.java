package com.fintracker.backend.fintrackermonolith.core.module.portfolio.api;

import com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.request.*;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.response.*;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.model.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping
    public ResponseEntity<CreatePortfolioResponse> createPortfolio(@RequestBody CreatePortfolioRequest request) {
        return ResponseEntity.ok(portfolioService.createPortfolio(
                request.userId(),
                request.name()
        ));
    }

    @GetMapping
    public ResponseEntity<GetPortfoliosByIdsResponse> getPortfoliosByIds(@RequestParam List<Long> idList) {
        return ResponseEntity.ok(portfolioService.getPortfoliosByIds(idList));
    }

    @PutMapping
    public ResponseEntity<UpdatePortfolioByIdResponse> updatePortfolioById(@RequestParam Long id, @RequestBody UpdatePortfolioByIdRequest request) {
        return ResponseEntity.ok(portfolioService.updatePortfolioById(
                id,
                request.userId(),
                request.name()
        ));
    }

    @DeleteMapping
    public ResponseEntity<DeletePortfoliosByIdsResponse> deletePortfoliosByIds(@RequestParam List<Long> idList) {
        return ResponseEntity.ok(portfolioService.deletePortfoliosByIds(idList));
    }

}
