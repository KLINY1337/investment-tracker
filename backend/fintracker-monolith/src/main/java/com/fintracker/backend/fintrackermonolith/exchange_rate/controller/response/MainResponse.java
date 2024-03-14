package com.fintracker.backend.fintrackermonolith.exchange_rate.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MainResponse<T> {
    private boolean isSuccess;
    private String message;
    private T data;

    public static <T> MainResponse<T> success (T data) {
        return MainResponse.<T>builder()
                .isSuccess(true)
                .data(data)
                .build();
    }

    public static <T> MainResponse<T> error (String message) {
        return MainResponse.<T>builder()
                .isSuccess(false)
                .message(message)
                .build();
    }
}
