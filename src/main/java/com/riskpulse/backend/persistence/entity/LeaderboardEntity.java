package com.riskpulse.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "leaderboard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderboardEntity {

    @EmbeddedId
    private LeaderboardId id;

    @Column(name = "rank", nullable = false)
    private Integer rank;

    @Column(name = "total_points", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPoints;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
