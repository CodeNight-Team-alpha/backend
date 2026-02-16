package com.riskpulse.backend.api;

import com.riskpulse.backend.application.RiskService;
import com.riskpulse.backend.domain.RiskItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Thin controller: delegates to service only.
 */
@RestController
@RequestMapping("/api/risks")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @GetMapping
    public List<RiskItem> list() {
        return riskService.list();
    }
}
