package com.riskpulse.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "challenges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeEntity {

    @Id
    @Column(name = "challenge_id", length = 50)
    private String challengeId;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "reward_points", nullable = false)
    private Integer rewardPoints;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
