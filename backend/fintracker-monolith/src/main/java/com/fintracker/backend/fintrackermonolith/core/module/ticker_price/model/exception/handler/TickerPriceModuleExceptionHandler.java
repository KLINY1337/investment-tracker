package com.fintracker.backend.fintrackermonolith.core.module.ticker_price.model.exception.handler;

import com.fintracker.backend.fintrackermonolith.core.module.ticker_price.model.exception.InvalidTickerPriceMarkDateException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TickerPriceModuleExceptionHandler {

    @ExceptionHandler(InvalidTickerPriceMarkDateException.class)
    public ProblemDetail handleInvalidTickerPriceDateException(InvalidTickerPriceMarkDateException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(422), e.getMessage());
    }
}
