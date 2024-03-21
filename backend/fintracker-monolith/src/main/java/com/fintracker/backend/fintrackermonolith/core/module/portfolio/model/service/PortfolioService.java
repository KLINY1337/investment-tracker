package com.fintracker.backend.fintrackermonolith.core.module.portfolio.model.service;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.service.UserService;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Asset;
import com.fintracker.backend.fintrackermonolith.core.db.entity.InvestmentPosition;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Portfolio;
import com.fintracker.backend.fintrackermonolith.core.db.repository.PortfolioRepository;
import com.fintracker.backend.fintrackermonolith.core.module.asset.model.service.AssetService;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.model.service.InvestmentPositionService;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.response.*;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.model.exception.PortfolioAlreadyExistsException;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.model.exception.PortfolioIdsNotFoundException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    private final UserService userService;
    //todo fix circular dependency below
//    private final InvestmentPositionService investmentPositionService;
    private final AssetService assetService;

    public CreatePortfolioResponse createPortfolio(Long userId, String name) {
        User user = userService.getUsersByIds(List.of(userId)).users().get(0);
        if (portfolioRepository.existsByUserAndName(user, name)) {
            throw new PortfolioAlreadyExistsException("Specified portfolio already exists for specified user");
        }

        Portfolio portfolio = Portfolio.builder()
                .user(user)
                .name(name)
                .build();

        return new CreatePortfolioResponse(
                true,
                "Portfolio successfully created",
                portfolioRepository.save(portfolio)
        );
    }

    public GetPortfoliosByIdsResponse getPortfoliosByIds(List<Long> ids) {
        Map<Boolean, List<Long>> processableAndUnprocessableIds = ids.stream()
                .collect(Collectors.partitioningBy(portfolioRepository::existsById));

        if (!processableAndUnprocessableIds.get(false).isEmpty()) {
            throw new PortfolioIdsNotFoundException("Specified idList contains values, that don't exist in database", processableAndUnprocessableIds.get(false));
        }

        return new GetPortfoliosByIdsResponse(
                true,
                "Specified portfolios retrieved from database",
                portfolioRepository.findAllById(processableAndUnprocessableIds.get(true))
        );
    }

    public UpdatePortfolioByIdResponse updatePortfolioById(Long id, Long userId, String name) {
        User user = userService.getUsersByIds(List.of(userId)).users().get(0);
        if (portfolioRepository.existsByUserAndName(user, name)) {
            throw new PortfolioAlreadyExistsException("Specified portfolio already exists for specified user");
        }
        Portfolio portfolio = portfolioRepository
                .findById(id)
                .orElseThrow(() -> new PortfolioIdsNotFoundException("Specified portfolio does not exist in database", List.of(id)));

        portfolio.setUser(user);
        portfolio.setName(name);

        return new UpdatePortfolioByIdResponse(
                true,
                "Portfolio successfully updated",
                portfolio
        );
    }

    public DeletePortfoliosByIdsResponse deletePortfoliosByIds(List<Long> ids) {
        Map<Boolean, List<Long>> processableAndUnprocessableIds = ids.stream()
                .collect(Collectors.partitioningBy(portfolioRepository::existsById));

        if (!processableAndUnprocessableIds.get(false).isEmpty()) {
            throw new PortfolioIdsNotFoundException("Specified idList contains values, that don't exist in database", processableAndUnprocessableIds.get(false));
        }

        portfolioRepository.deleteAllById(processableAndUnprocessableIds.get(true));
        return new DeletePortfoliosByIdsResponse(
                true,
                "Specified portfolios deleted from database",
                processableAndUnprocessableIds.get(true).size()
        );
    }

    public GetTotalPortfoliosPriceByUserId getTotalPortfoliosPriceByUserId(Long userId, Long quoteAssetId) {
        //TODO fix circular dependency
        //┌─────┐
        //|  investmentPositionService defined in file [C:\Users\Professional\Desktop\investment-tracker\backend\fintracker-monolith\target\classes\com\fintracker\backend\fintrackermonolith\core\module\investment_position\model\service\InvestmentPositionService.class]
        //↑     ↓
        //|  portfolioService defined in file [C:\Users\Professional\Desktop\investment-tracker\backend\fintracker-monolith\target\classes\com\fintracker\backend\fintrackermonolith\core\module\portfolio\model\service\PortfolioService.class]
        //└─────┘
//        User user = userService.getUsersByIds(List.of(userId)).users().get(0);
//        Asset asset = assetService.getAssetsByIds(List.of(quoteAssetId)).assets().get(0);
//        List<Long> portfoliosIds = portfolioRepository.findAllIdsByUser(user);
//        List<InvestmentPosition> investmentPositions = investmentPositionService
//                .getInvestmentPositionsByPortfoliosIds(portfoliosIds)
//                .investmentPositions();
//        List<BigDecimal> investmentPositionsPrices = new ArrayList<>();
//        investmentPositions.forEach(investmentPosition -> {
//            if (investmentPosition.getCloseDate() == null) {
//                // TODO лешины конвертеры поставить еще сверху
//            }
//            else {
//                // TODO лешины конвертеры поставить еще сверху
//                investmentPositionsPrices.add(investmentPosition.getCloseQuoteAssetPrice().multiply(investmentPosition.getBaseAssetAmount()));
//            }
//        });
//        Optional<BigDecimal> totalPortfoliosPriceOptional = investmentPositionsPrices
//                .parallelStream()
//                .reduce(BigDecimal::add);

        return new GetTotalPortfoliosPriceByUserId(
                true,
                "Total price of user portfolios has been calculated",
//                totalPortfoliosPriceOptional.orElse(BigDecimal.ZERO)
                BigDecimal.ZERO
        );
    }

    public GetPortfoliosCountByUserIdResponse getPortfoliosCountByUserId(Long userId) {
        User user = userService.getUsersByIds(List.of(userId)).users().get(0);
        return new GetPortfoliosCountByUserIdResponse(
                true,
                "Portfolios of user successfully counted",
                portfolioRepository.countByUser(user)
        );
    }
}
