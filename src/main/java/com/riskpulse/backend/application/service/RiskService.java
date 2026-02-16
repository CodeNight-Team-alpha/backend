package com.riskpulse.backend.application.service;

import com.riskpulse.backend.domain.model.RiskItem;
import com.riskpulse.backend.port.RiskStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service. No business logic yet; delegates to store.
 */
@Service
@RequiredArgsConstructor
public class RiskService {

    private final RiskStore riskStore;

    public List<RiskItem> list() {
        return riskStore.findAll();
    }
}
