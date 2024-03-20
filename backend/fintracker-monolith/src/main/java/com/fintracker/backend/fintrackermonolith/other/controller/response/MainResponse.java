package com.fintracker.backend.fintrackermonolith.other.controller.response;

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

    public static <T> MainResponse<T> success (T data) {
        return MainResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> MainResponse<T> error (String message) {
        return MainResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}