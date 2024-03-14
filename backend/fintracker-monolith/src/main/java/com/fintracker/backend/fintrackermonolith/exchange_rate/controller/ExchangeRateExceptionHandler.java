package com.fintracker.backend.fintrackermonolith.exchange_rate.controller;


import com.fintracker.backend.fintrackermonolith.exchange_rate.exception.cbr.CBRRatesException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExchangeRateExceptionHandler {
    @ExceptionHandler(CBRRatesException.class)
    public ProblemDetail handleCBRRatesException(CBRRatesException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), e.getMessage());
        problemDetail.setDetail("CBRRatesException");
        return problemDetail;
    }
}
