package com.riskpulse.backend.domain.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

/** Domain model for user_state. Pure Java, no framework. Null-safe factory for metrics. */
@Value
@Builder
public class UserState {

    BigDecimal spendToday;
    int uniqueCategoriesToday;
    BigDecimal electronicsSpendToday;
    BigDecimal spend7d;

    /** Builds UserState from raw metrics; nulls become ZERO so domain stays null-safe. */
    public static UserState fromMetrics(
            BigDecimal spendToday,
            Number uniqueCategoriesToday,
            BigDecimal electronicsSpendToday,
            BigDecimal spend7d) {
        return UserState.builder()
                .spendToday(spendToday != null ? spendToday : BigDecimal.ZERO)
                .uniqueCategoriesToday(uniqueCategoriesToday != null ? uniqueCategoriesToday.intValue() : 0)
                .electronicsSpendToday(electronicsSpendToday != null ? electronicsSpendToday : BigDecimal.ZERO)
                .spend7d(spend7d != null ? spend7d : BigDecimal.ZERO)
                .build();
    }
}
