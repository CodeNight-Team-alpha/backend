package com.riskpulse.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "challenge_awards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeAwardEntity {

    @Id
    @Column(name = "award_id", length = 50)
    private String awardId;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "as_of_date", nullable = false)
    private LocalDate asOfDate;

    @Column(name = "challenge_id", length = 50)
    private String challengeId;

    @Column(name = "triggered_challenges", columnDefinition = "TEXT")
    private String triggeredChallenges;

    @Column(name = "selected_challenge", length = 50)
    private String selectedChallenge;

    @Column(name = "suppressed_challenges", columnDefinition = "TEXT")
    private String suppressedChallenges;

    @Column(name = "reward_points")
    private Integer rewardPoints;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
