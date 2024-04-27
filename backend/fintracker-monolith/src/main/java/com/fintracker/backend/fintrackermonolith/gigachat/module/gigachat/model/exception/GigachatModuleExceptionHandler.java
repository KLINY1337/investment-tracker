package com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.model.exception;

import com.fintracker.backend.fintrackermonolith.core.module.ticker.model.exception.TickerAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GigachatModuleExceptionHandler {
    @ExceptionHandler(GigachatTokenException.class)
    public ProblemDetail handleAssetAlreadyExistsException(GigachatTokenException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), e.getMessage());
    }
}
