package com.fintracker.backend.fintrackermonolith.core.module.asset.model.exception.handler;

import com.fintracker.backend.fintrackermonolith.core.module.asset.model.exception.AssetAlreadyExistsException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AssetModuleExceptionHandler {

    @ExceptionHandler(AssetAlreadyExistsException.class)
    public ProblemDetail handleAssetAlreadyExistsException(AssetAlreadyExistsException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409), e.getMessage());
    }
}
