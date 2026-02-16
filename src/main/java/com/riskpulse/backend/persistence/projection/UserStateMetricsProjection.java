package com.riskpulse.backend.persistence.projection;

import java.math.BigDecimal;

/**
 * Projection for aggregation query result (user_id + 4 metrics).
 */
public interface UserStateMetricsProjection {

    String getUserId();

    BigDecimal getSpendToday();

    /** COUNT(DISTINCT ...) may come as Long from DB */
    Number getUniqueCategoriesToday();

    BigDecimal getElectronicsSpendToday();

    BigDecimal getSpend7d();
}
