package com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GigachatModelResponse {
    private List<GigachatChoice> choices;
    private long created;
    private String model;
    private Object usage;
    private String object;
}
