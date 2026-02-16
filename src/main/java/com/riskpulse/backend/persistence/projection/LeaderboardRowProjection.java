package com.riskpulse.backend.persistence.projection;

import java.math.BigDecimal;

/**
 * Projection for leaderboard read with user display name.
 */
public interface LeaderboardRowProjection {

    int getRank();

    String getUserId();

    String getDisplayName();

    BigDecimal getTotalPoints();
}
