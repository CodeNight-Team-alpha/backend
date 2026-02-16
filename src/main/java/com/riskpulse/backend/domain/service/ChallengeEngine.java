package com.riskpulse.backend.domain.service;

import com.riskpulse.backend.domain.model.ChallengeDecision;
import com.riskpulse.backend.domain.model.ChallengeRule;
import com.riskpulse.backend.domain.model.UserState;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Deterministic challenge evaluator. No Spring/JPA.
 * Kurallar (tabloya göre): C-01 spend_today>=200, C-02 unique_categories_today>=3,
 * C-03 electronics_spend_today>=300, C-04 spend_7d>=1500.
 * Seçilen = tetiklenenler arasında en yüksek öncelikli (priority en küçük).
 */
public final class ChallengeEngine {

    private static final BigDecimal THRESHOLD_200 = new BigDecimal("200");
    private static final BigDecimal THRESHOLD_300 = new BigDecimal("300");
    private static final BigDecimal THRESHOLD_1500 = new BigDecimal("1500");
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
            case "C-01": // Günlük Harcama: spend_today >= 200
                return state.getSpendToday() != null && state.getSpendToday().compareTo(THRESHOLD_200) >= 0;
            case "C-02": // Kategori Avcısı: unique_categories_today >= 3
                return state.getUniqueCategoriesToday() >= THRESHOLD_CATEGORIES;
            case "C-03": // Elektronik Bonus: electronics_spend_today >= 300
                return state.getElectronicsSpendToday() != null && state.getElectronicsSpendToday().compareTo(THRESHOLD_300) >= 0;
            case "C-04": // Haftalık Aktif: spend_7d >= 1500
                return state.getSpend7d() != null && state.getSpend7d().compareTo(THRESHOLD_1500) >= 0;
            default:
                return false;
        }
    }
}
