package com.fintracker.backend.fintrackermonolith.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MainResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
