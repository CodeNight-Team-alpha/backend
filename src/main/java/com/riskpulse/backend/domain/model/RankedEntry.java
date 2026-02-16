package com.riskpulse.backend.domain.model;

import lombok.Value;

import java.math.BigDecimal;

/** Domain value: leaderboard row with rank. displayName is presentation and set in application. */
@Value
public class RankedEntry {

    int rank;
    String userId;
    BigDecimal totalPoints;
}
