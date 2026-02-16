package com.riskpulse.backend.domain.model;

import lombok.Value;

/** Domain model for a challenge (id + reward + priority). Spring/JPA free. */
@Value
public class ChallengeRule {

    String challengeId;
    int rewardPoints;
    int priority;
}
