package com.riskpulse.backend.domain.model;

import lombok.Value;

/**
 * Domain value object for a badge (config). No framework dependencies.
 */
@Value
public class Badge {

    String badgeId;
    String badgeName;
    int thresholdPoints;
}
