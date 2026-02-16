package com.riskpulse.backend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain entity. Business logic (e.g. RiskEngine) will be added later.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskItem {

    private String id;
    private String name;
}
