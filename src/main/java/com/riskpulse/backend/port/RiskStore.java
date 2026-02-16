package com.riskpulse.backend.port;

import com.riskpulse.backend.domain.RiskItem;

import java.util.List;

/**
 * Port (out): persistence / store contract.
 */
public interface RiskStore {

    List<RiskItem> findAll();
}
