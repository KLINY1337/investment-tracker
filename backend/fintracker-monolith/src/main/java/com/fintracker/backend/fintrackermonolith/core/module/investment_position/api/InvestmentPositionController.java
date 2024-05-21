package com.fintracker.backend.fintrackermonolith.core.module.investment_position.api;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;
import com.fintracker.backend.fintrackermonolith.auth_server.db.repository.UserRepository;
import com.fintracker.backend.fintrackermonolith.auth_server.util.AccessTokenUtils;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.request.CreateInvestmentPositionRequest;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.request.UpdateInvestmentPositionByIdRequest;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.api.response.*;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.model.service.InvestmentPositionService;
import com.fintracker.backend.fintrackermonolith.core.util.RequestParamUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/investment_positions")
@RequiredArgsConstructor
@CrossOrigin
public class InvestmentPositionController {

    private final InvestmentPositionService investmentPositionService;
    private final UserRepository userRepository;

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
    public ResponseEntity<GetInvestmentPositionsResponse> getInvestmentPositionsByIds(@RequestParam String idList) {
        return ResponseEntity.ok(investmentPositionService.getInvestmentPositionsByIds(
                RequestParamUtils.parseParamAsList(idList, ",", Long::valueOf)
        ));
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
    public ResponseEntity<DeleteInvestmentPositionsByIdsResponse> deleteInvestmentPositionsByIds(@RequestParam String idList) {
        return ResponseEntity.ok(investmentPositionService.deleteInvestmentPositionsByIds(
                RequestParamUtils.parseParamAsList(idList, ",", Long::valueOf)
        ));
    }

    @GetMapping("/byPortfoliosIds")
    public ResponseEntity<GetInvestmentPositionsByPortfoliosIdsResponse> getInvestmentPositionsByPortfolios(@RequestParam String idList) {
        return ResponseEntity.ok(investmentPositionService.getInvestmentPositionsByPortfoliosIds(
                RequestParamUtils.parseParamAsList(idList, ",", Long::valueOf)
        ));
    }

    @GetMapping("/price")
    public ResponseEntity<GetInvestmentPositionPriceByIdResponse> getInvestmentPositionPriceById(@RequestParam Long investmentPositionId,
                                                                                                 @RequestParam Long quoteAssetId,
                                                                                                 @RequestParam Date quotationDate) {
        return ResponseEntity.ok(investmentPositionService.getInvestmentPositionPriceById(
                investmentPositionId,
                quoteAssetId,
                quotationDate
        ));
    }

    @GetMapping("/price/total/byPortfolioId")
    public ResponseEntity<GetInvestmentPositionsTotalPriceByPortfolioIdResponse> getInvestmentPositionsTotalPriceByPortfolioId(@RequestParam Long portfolioId,
                                                                                                                               @RequestParam Long quoteAssetId,
                                                                                                                               @RequestParam Date quotationDate) {
        return ResponseEntity.ok(investmentPositionService.getInvestmentPositionsTotalPriceByPortfolioId(
                portfolioId,
                quoteAssetId,
                quotationDate
        ));
    }

    @GetMapping("/price/total")
    public ResponseEntity<GetInvestmentPositionsTotalPriceByUserIdResponse> getInvestmentPositionsTotalPriceByUserId(@RequestHeader("Authorization") String authorizationHeader) {
        Optional<User> user = userRepository.findUserByUsernameOrEmail(AccessTokenUtils.getUsernameFromToken(authorizationHeader.substring(7)));
        return ResponseEntity.ok(investmentPositionService.getInvestmentPositionsTotalPriceByUserId(user.get().getId()));
    }

    @GetMapping("/profit/byPortfolioId")
    public ResponseEntity<GetProfitByPortfolioIdResponse> getProfitByPortfolioId(@RequestParam Long portfolioId) {
        return ResponseEntity.ok(investmentPositionService.getProfitByPortfolioId(portfolioId));
    }

    @GetMapping("/distribution")
    public ResponseEntity<GetInvestmentPositionsDistributionByUserIdResponse> getInvestmentPositionsDistributionByUserId(@RequestHeader("Authorization") String authorizationHeader) {
        Optional<User> user = userRepository.findUserByUsernameOrEmail(AccessTokenUtils.getUsernameFromToken(authorizationHeader.substring(7)));
        return ResponseEntity.ok(investmentPositionService.getInvestmentPositionsDistributionByUserId(user.get().getId()));
    }

    @GetMapping("/price/total/byPortfolioIdAndDate")
    public ResponseEntity<GetTotalPortfolioPriceByPortfolioIdInTimeResponse> getTotalPortfolioPriceByPortfolioIdAndDate(@RequestParam Long portfolioId,
                                                                                                                        @RequestParam Date date) {
        return ResponseEntity.ok(investmentPositionService.getTotalPortfolioPriceByPortfolioIdAndDate(portfolioId, date));
    }

}
