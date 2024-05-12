package com.fintracker.backend.fintrackermonolith.core.module.portfolio.api;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;
import com.fintracker.backend.fintrackermonolith.auth_server.db.repository.UserRepository;
import com.fintracker.backend.fintrackermonolith.auth_server.util.AccessTokenUtils;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Portfolio;
import com.fintracker.backend.fintrackermonolith.core.db.repository.InvestmentPositionRepository;
import com.fintracker.backend.fintrackermonolith.core.db.repository.PortfolioRepository;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.request.*;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.response.*;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.model.service.PortfolioService;
import com.fintracker.backend.fintrackermonolith.core.util.RequestParamUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final InvestmentPositionRepository investmentPositionRepository;

    @PostMapping
    public ResponseEntity<CreatePortfolioResponse> createPortfolio(@RequestHeader("Authorization") String authorizationHeader,
                                                                   @RequestBody CreatePortfolioRequest request) {
        Optional<User> user = userRepository.findUserByUsernameOrEmail(AccessTokenUtils.getUsernameFromToken(authorizationHeader.substring(7)));
        return ResponseEntity.ok(portfolioService.createPortfolio(
                user.get().getId(),
                request.name()
        ));
    }

    @GetMapping("/byIds")
    public ResponseEntity<GetPortfoliosByIdsResponse> getPortfoliosByIds(@RequestParam String idList) {
        return ResponseEntity.ok(portfolioService.getPortfoliosByIds(
                RequestParamUtils.parseParamAsList(idList, ",", Long::valueOf)
        ));
    }

    @PutMapping
    public ResponseEntity<UpdatePortfolioByIdResponse> updatePortfolioById(@RequestParam Long id, @RequestBody UpdatePortfolioByIdRequest request,
                                                                           @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(portfolioService.updatePortfolioById(
                id,
                authorizationHeader,
                request.name()
        ));
    }

    @DeleteMapping
    public ResponseEntity<DeletePortfoliosByIdsResponse> deletePortfoliosByIds(@RequestParam String idList) {
        return ResponseEntity.ok(portfolioService.deletePortfoliosByIds(
                RequestParamUtils.parseParamAsList(idList, ",", Long::valueOf)
        ));
    }

    @GetMapping("/count")
    public ResponseEntity<GetPortfoliosCountByUserIdResponse> getPortfoliosCountByUser(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(portfolioService.getPortfoliosCountByUserId(authorizationHeader));
    }

    @GetMapping
    public ResponseEntity<List<Portfolio>> getPortfoliosByUserId(@RequestHeader("Authorization") String authorizationHeader) {
        Optional<User> user = userRepository.findUserByUsernameOrEmail(AccessTokenUtils.getUsernameFromToken(authorizationHeader.substring(7)));
        return ResponseEntity.ok(portfolioService.getPortfoliosByUserId(user.get().getId()));
    }

    @GetMapping("/chart/price")
    public ResponseEntity<List<BigDecimal>> getPortfolioPriceChartData(@RequestParam Long portfolioId) {
        return ResponseEntity.ok(investmentPositionRepository.findAllByPortfolios(List.of(portfolioRepository.findById(portfolioId).get())).stream()
                .map(investmentPosition -> investmentPosition.getBaseAssetAmount().multiply(investmentPosition.getOpenQuoteAssetPrice()))
                .toList());
    }
}
