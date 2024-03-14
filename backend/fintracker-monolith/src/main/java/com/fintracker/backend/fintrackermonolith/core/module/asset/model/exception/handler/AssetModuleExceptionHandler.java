package com.fintracker.backend.fintrackermonolith.core.module.asset.model.exception.handler;

import com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.exception.UserIdsNotFoundException;
import com.fintracker.backend.fintrackermonolith.core.module.asset.model.exception.AssetAlreadyExistsException;
import com.fintracker.backend.fintrackermonolith.core.module.asset.model.exception.AssetIdsNotFoundException;
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

    @ExceptionHandler(AssetIdsNotFoundException.class)
    public ProblemDetail handleAssetIdsNotFoundException(AssetIdsNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(422), e.getMessage());
        problemDetail.setProperty("irretrievableIds", e.getUnprocessableIds());
        return problemDetail;
    }
}
