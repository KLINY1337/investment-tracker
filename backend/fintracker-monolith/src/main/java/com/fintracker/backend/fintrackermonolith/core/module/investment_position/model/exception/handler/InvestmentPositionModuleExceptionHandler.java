package com.fintracker.backend.fintrackermonolith.core.module.investment_position.model.exception.handler;

import com.fintracker.backend.fintrackermonolith.core.module.investment_position.model.exception.InvalidTimePeriodException;
import com.fintracker.backend.fintrackermonolith.core.module.investment_position.model.exception.InvestmentPositionIdsNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvestmentPositionModuleExceptionHandler {

    @ExceptionHandler(InvalidTimePeriodException.class)
    public ProblemDetail handleInvalidTimePeriodException(InvalidTimePeriodException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(422), e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(InvestmentPositionIdsNotFoundException.class)
    public ProblemDetail handleAssetIdsNotFoundException(InvestmentPositionIdsNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(422), e.getMessage());
        problemDetail.setProperty("irretrievableIds", e.getUnprocessableIds());
        return problemDetail;
    }
}
