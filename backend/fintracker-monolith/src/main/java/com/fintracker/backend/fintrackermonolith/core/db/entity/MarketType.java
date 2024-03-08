package com.fintracker.backend.fintrackermonolith.core.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "market_types")
public class MarketType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
