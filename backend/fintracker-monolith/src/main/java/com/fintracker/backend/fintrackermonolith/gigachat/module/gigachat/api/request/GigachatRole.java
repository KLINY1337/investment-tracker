package com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api.request;

public enum GigachatRole {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant"),
    FUNCTION("function");

    private final String value;

    GigachatRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}