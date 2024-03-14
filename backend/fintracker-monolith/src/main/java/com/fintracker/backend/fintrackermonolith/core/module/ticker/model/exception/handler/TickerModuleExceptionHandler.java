package com.fintracker.backend.fintrackermonolith.core.module.ticker.model.exception.handler;

import com.fintracker.backend.fintrackermonolith.core.module.ticker.model.exception.TickerAlreadyExistsException;
import com.fintracker.backend.fintrackermonolith.core.module.ticker.model.exception.TickerIdsNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TickerModuleExceptionHandler {

    @ExceptionHandler(TickerAlreadyExistsException.class)
    public ProblemDetail handleAssetAlreadyExistsException(TickerAlreadyExistsException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409), e.getMessage());
    }

    @ExceptionHandler(TickerIdsNotFoundException.class)
    public ProblemDetail handleAssetIdsNotFoundException(TickerIdsNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(422), e.getMessage());
        problemDetail.setProperty("irretrievableIds", e.getUnprocessableIds());
        return problemDetail;
    }
}
