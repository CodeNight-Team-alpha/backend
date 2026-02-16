package com.riskpulse.backend.api.controller;

import com.riskpulse.backend.application.service.RiskService;
import com.riskpulse.backend.domain.model.RiskItem;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Thin controller: delegates to service only.
 */
@RestController
@RequestMapping("/api/risks")
@RequiredArgsConstructor
public class RiskController {

    private final RiskService riskService;

    @GetMapping
    public List<RiskItem> list() {
        return riskService.list();
    }
}
