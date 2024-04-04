package com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.api.request;

import com.fintracker.backend.fintrackermonolith.gigachat.module.gigachat.model.dto.GigachatMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.util.List;

@Getter
@Setter
public class GigachatRequest {
    String model;
    List<GigachatMessage> messages;

}
