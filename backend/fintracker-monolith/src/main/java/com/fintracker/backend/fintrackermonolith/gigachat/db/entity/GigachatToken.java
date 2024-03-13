package com.fintracker.backend.fintrackermonolith.gigachat.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class GigachatToken {

    @Id
    public String access_token;
    public String expires_at;
}