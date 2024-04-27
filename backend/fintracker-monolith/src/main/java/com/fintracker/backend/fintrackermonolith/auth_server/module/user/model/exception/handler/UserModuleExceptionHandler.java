package com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.exception.handler;

import com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.exception.UserAlreadyExistsException;
import com.fintracker.backend.fintrackermonolith.auth_server.module.user.model.exception.UserIdsNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class UserModuleExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ProblemDetail handleUserAlreadyExistsException(UsernameNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409), e.getMessage());
    }

    @ExceptionHandler(UserIdsNotFoundException.class)
    public ProblemDetail handleUserAlreadyExistsException(UserIdsNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(422), e.getMessage());
        problemDetail.setProperty("irretrievableIds", e.getUnprocessableIds());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(422), e.getMessage());
        problemDetail.setProperty("invalidFields", errors);
        return problemDetail;
    }
}
