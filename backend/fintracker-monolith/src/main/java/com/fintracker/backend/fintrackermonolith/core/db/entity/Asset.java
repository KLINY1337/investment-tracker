package com.fintracker.backend.fintrackermonolith.core.db.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assets")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    private String asset_type_name;
}