package com.riskpulse.backend.domain.service;

import com.riskpulse.backend.domain.model.ChallengeDecision;
import com.riskpulse.backend.domain.model.ChallengeRule;
import com.riskpulse.backend.domain.model.UserState;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Deterministic challenge evaluator. No Spring/JPA.
 * Rules (by challenge_id): C-01 spend_today>=200, C-02 spend_7d>=3000,
 * C-03 unique_categories_today>=3, C-04 electronics_spend_today>=200.
 * Selected = triggered with smallest priority; others suppressed.
 */
public final class ChallengeEngine {

    private static final BigDecimal THRESHOLD_200 = new BigDecimal("200");
    private static final BigDecimal THRESHOLD_3000 = new BigDecimal("3000");
    private static final int THRESHOLD_CATEGORIES = 3;

    public static ChallengeDecision evaluate(UserState state, List<ChallengeRule> challengesOrderedByPriority) {
        List<String> triggered = new ArrayList<>();
        for (ChallengeRule c : challengesOrderedByPriority) {
            if (passes(state, c.getChallengeId())) {
                triggered.add(c.getChallengeId());
            }
        }
        if (triggered.isEmpty()) {
            return new ChallengeDecision(
                    List.of(),
                    null,
                    List.of(),
                    0
            );
        }
        String selected = triggered.get(0);
        int rewardPoints = 0;
        for (ChallengeRule c : challengesOrderedByPriority) {
            if (c.getChallengeId().equals(selected)) {
                rewardPoints = c.getRewardPoints();
                break;
            }
        }
        List<String> suppressed = triggered.size() > 1 ? triggered.subList(1, triggered.size()) : List.of();
        return new ChallengeDecision(
                List.copyOf(triggered),
                selected,
                suppressed,
                rewardPoints
        );
    }

    private static boolean passes(UserState state, String challengeId) {
        switch (challengeId) {
            case "C-01":
                return state.getSpendToday() != null && state.getSpendToday().compareTo(THRESHOLD_200) >= 0;
            case "C-02":
                return state.getSpend7d() != null && state.getSpend7d().compareTo(THRESHOLD_3000) >= 0;
            case "C-03":
                return state.getUniqueCategoriesToday() >= THRESHOLD_CATEGORIES;
            case "C-04":
                return state.getElectronicsSpendToday() != null && state.getElectronicsSpendToday().compareTo(THRESHOLD_200) >= 0;
            default:
                return false;
        }
    }
}
