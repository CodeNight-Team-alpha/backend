package com.riskpulse.backend.domain.model;

import lombok.Value;

import java.math.BigDecimal;

/** Domain value: user id and total points. No framework. */
@Value
public class UserPoints {

    String userId;
    BigDecimal totalPoints;
}
