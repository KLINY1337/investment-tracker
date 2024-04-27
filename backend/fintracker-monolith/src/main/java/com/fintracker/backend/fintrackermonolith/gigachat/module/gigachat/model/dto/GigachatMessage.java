package com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GigachatMessage {
    private String role;
    private String content;
    private List<Object> dataForContext;
}
