package com.riskpulse.backend.application.service;

import com.riskpulse.backend.domain.model.RiskItem;
import com.riskpulse.backend.port.RiskStore;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service. No business logic yet; delegates to store.
 */
@Service
public class RiskService {

    private final RiskStore riskStore;

    public RiskService(RiskStore riskStore) {
        this.riskStore = riskStore;
    }

    public List<RiskItem> list() {
        return riskStore.findAll();
    }
}
