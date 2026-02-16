package com.riskpulse.backend.persistence.projection;

import java.math.BigDecimal;

/**
 * Projection for points_ledger aggregation: user_id and total points (as of a date).
 */
public interface UserPointsProjection {

    String getUserId();

    BigDecimal getTotalPoints();
}
