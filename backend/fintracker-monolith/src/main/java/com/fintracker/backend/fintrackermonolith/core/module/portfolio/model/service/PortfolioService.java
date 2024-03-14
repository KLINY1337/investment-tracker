package com.fintracker.backend.fintrackermonolith.core.module.portfolio.model.service;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.service.UserService;
import com.fintracker.backend.fintrackermonolith.core.db.entity.Portfolio;
import com.fintracker.backend.fintrackermonolith.core.db.repository.PortfolioRepository;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.response.CreatePortfolioResponse;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.response.DeletePortfoliosByIdsResponse;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.response.GetPortfoliosByIdsResponse;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.api.response.UpdatePortfolioByIdResponse;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.model.exception.PortfolioAlreadyExistsException;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.model.exception.PortfolioIdsNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    UserService userService;

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
}
