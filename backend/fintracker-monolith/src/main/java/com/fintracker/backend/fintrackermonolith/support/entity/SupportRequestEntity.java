package com.fintracker.backend.fintrackermonolith.support.entity;

import com.fintracker.backend.fintrackermonolith.support.enumeration.SupportRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Table
public class SupportRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;
    private String email;
    private String name;
    private Date createdAt;

    private SupportRequestStatus status;

}
