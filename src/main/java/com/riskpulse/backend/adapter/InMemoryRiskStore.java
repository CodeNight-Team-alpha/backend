package com.riskpulse.backend.adapter;

import com.riskpulse.backend.domain.model.RiskItem;
import com.riskpulse.backend.port.RiskStore;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * In-memory adapter for RiskStore. Sample data for hackathon bootstrap.
 */
@Component
public class InMemoryRiskStore implements RiskStore {

    private static final List<RiskItem> SAMPLE = List.of(
            new RiskItem("1", "Sample Risk A"),
            new RiskItem("2", "Sample Risk B"),
            new RiskItem("3", "Sample Risk C")
    );

    @Override
    public List<RiskItem> findAll() {
        return SAMPLE;
    }
}
