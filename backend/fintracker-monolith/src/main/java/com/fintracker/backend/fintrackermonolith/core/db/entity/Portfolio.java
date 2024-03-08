package com.fintracker.backend.fintrackermonolith.core.db.entity;

import com.fintracker.backend.fintrackermonolith.auth_server.db.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "portfolios")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;
}
