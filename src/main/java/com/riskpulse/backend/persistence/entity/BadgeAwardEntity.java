package com.riskpulse.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "badge_awards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeAwardEntity {

    @EmbeddedId
    private BadgeAwardId id;

    @Column(name = "award_id", length = 50, unique = true)
    private String awardId;

    @Column(name = "awarded_at", nullable = false)
    private OffsetDateTime awardedAt;
}
