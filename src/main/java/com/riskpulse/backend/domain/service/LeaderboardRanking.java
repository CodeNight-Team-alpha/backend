package com.riskpulse.backend.domain.service;

import com.riskpulse.backend.domain.model.RankedEntry;
import com.riskpulse.backend.domain.model.UserPoints;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Pure domain: assign ranks to an ordered list of user points.
 * Input must be ordered by totalPoints descending (caller/DB responsibility).
 */
public final class LeaderboardRanking {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private LeaderboardRanking() {}

    /**
     * Returns ranked entries (rank 1, 2, 3...) from ordered user points.
     */
    public static List<RankedEntry> assignRanks(List<UserPoints> orderedByPointsDesc) {
        List<RankedEntry> result = new ArrayList<>();
        int rank = 1;
        for (UserPoints up : orderedByPointsDesc) {
            BigDecimal total = up.getTotalPoints() != null ? up.getTotalPoints() : ZERO;
            result.add(new RankedEntry(rank++, up.getUserId(), total));
        }
        return List.copyOf(result);
    }
}
