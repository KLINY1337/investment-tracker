package com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.model.dto.GigachatMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GigachatChoice {
    private GigachatMessage message;
    private String content;
    @JsonProperty("data_for_context")
    private List<Object> dataForContext;
}
