package com.riskpulse.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "badges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeEntity {

    @Id
    @Column(name = "badge_id", length = 50)
    private String badgeId;

    @Column(name = "badge_name", nullable = false, length = 100)
    private String badgeName;

    @Column(name = "threshold_points", nullable = false)
    private Integer thresholdPoints;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
