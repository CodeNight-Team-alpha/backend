package com.riskpulse.backend.domain.model;

import lombok.Value;

import java.util.List;
import java.util.Optional;

/** Result of evaluating challenges for one user. Pure domain, no framework. */
@Value
public class ChallengeDecision {

    List<String> triggered;
    String selected;  // challenge_id or null when none triggered
    List<String> suppressed;
    int rewardPoints;

    /** Null-safe access to selected challenge id. */
    public Optional<String> getSelectedOptional() {
        return Optional.ofNullable(selected);
    }
}
