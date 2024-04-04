package com.fintracker.backend.fintrackermonolith.gigachat.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@ToString
public class GigachatToken {

    @Id
    @Column(length = 32768)
    public String access_token;
    public Timestamp expires_at;
}