package com.riskpulse.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "points_ledger")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointsLedgerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "points", nullable = false, precision = 12, scale = 2)
    private BigDecimal pointsDelta;

    @Column(name = "source", nullable = false, length = 50)
    private String source;

    @Column(name = "source_ref", nullable = false, length = 50)
    private String sourceRef;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
