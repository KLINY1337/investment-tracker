package com.fintracker.backend.fintrackermonolith.core.module.portfolio.model.exception.handler;

import com.fintracker.backend.fintrackermonolith.core.module.portfolio.model.exception.PortfolioAlreadyExistsException;
import com.fintracker.backend.fintrackermonolith.core.module.portfolio.model.exception.PortfolioIdsNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PortfolioModuleExceptionHandler {

    @ExceptionHandler(PortfolioAlreadyExistsException.class)
    public ProblemDetail handleAssetAlreadyExistsException(PortfolioAlreadyExistsException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409), e.getMessage());
    }

    @ExceptionHandler(PortfolioIdsNotFoundException.class)
    public ProblemDetail handleAssetIdsNotFoundException(PortfolioIdsNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(422), e.getMessage());
        problemDetail.setProperty("irretrievableIds", e.getUnprocessableIds());
        return problemDetail;
    }
}
