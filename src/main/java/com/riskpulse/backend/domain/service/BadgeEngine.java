package com.riskpulse.backend.domain.service;

import com.riskpulse.backend.domain.model.Badge;

import java.util.ArrayList;
import java.util.List;

/**
 * Pure domain: which badges a user has earned by total points.
 * No Spring/JPA. Badges must be ordered by threshold ascending (caller responsibility).
 */
public final class BadgeEngine {

    private BadgeEngine() {}

    /**
     * Returns badges the user has earned (totalPoints >= threshold).
     * Assumes badgesOrderedByThreshold is sorted by threshold ascending.
     */
    public static List<Badge> earnedBadges(int totalPoints, List<Badge> badgesOrderedByThreshold) {
        List<Badge> earned = new ArrayList<>();
        for (Badge b : badgesOrderedByThreshold) {
            if (totalPoints >= b.getThresholdPoints()) {
                earned.add(b);
            }
        }
        return List.copyOf(earned);
    }
}
