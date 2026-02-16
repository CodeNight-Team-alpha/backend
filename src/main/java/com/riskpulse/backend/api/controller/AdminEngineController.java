package com.riskpulse.backend.api.controller;

import com.riskpulse.backend.api.dto.EngineRunResponse;
import com.riskpulse.backend.application.service.EngineOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Thin controller: request param + single orchestrator call + response.
 */
@RestController
@RequestMapping("/api/v1/admin/engine")
@RequiredArgsConstructor
public class AdminEngineController {

    private final EngineOrchestrator engineOrchestrator;

    @PostMapping("/run")
    public ResponseEntity<EngineRunResponse> run(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        LocalDate resolved = engineOrchestrator.resolveAsOfDate(asOfDate);
        engineOrchestrator.runAll(asOfDate);
        return ResponseEntity.ok(new EngineRunResponse(resolved.toString(), "OK"));
    }

    @PostMapping("/metrics")
    public ResponseEntity<EngineRunResponse> metrics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        engineOrchestrator.runMetrics(asOfDate);
        LocalDate resolved = engineOrchestrator.resolveAsOfDate(asOfDate);
        return ResponseEntity.ok(new EngineRunResponse(resolved.toString(), "OK"));
    }

    @PostMapping("/challenges")
    public ResponseEntity<EngineRunResponse> challenges(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        engineOrchestrator.runChallenges(asOfDate);
        LocalDate resolved = engineOrchestrator.resolveAsOfDate(asOfDate);
        return ResponseEntity.ok(new EngineRunResponse(resolved.toString(), "OK"));
    }

    @PostMapping("/points")
    public ResponseEntity<EngineRunResponse> points(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        engineOrchestrator.runPoints(asOfDate);
        LocalDate resolved = engineOrchestrator.resolveAsOfDate(asOfDate);
        return ResponseEntity.ok(new EngineRunResponse(resolved.toString(), "OK"));
    }

    @PostMapping("/badges")
    public ResponseEntity<EngineRunResponse> badges(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        engineOrchestrator.runBadges(asOfDate);
        LocalDate resolved = engineOrchestrator.resolveAsOfDate(asOfDate);
        return ResponseEntity.ok(new EngineRunResponse(resolved.toString(), "OK"));
    }

    @PostMapping("/leaderboard")
    public ResponseEntity<EngineRunResponse> leaderboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        engineOrchestrator.runLeaderboard(asOfDate);
        LocalDate resolved = engineOrchestrator.resolveAsOfDate(asOfDate);
        return ResponseEntity.ok(new EngineRunResponse(resolved.toString(), "OK"));
    }

    @PostMapping("/notifications")
    public ResponseEntity<EngineRunResponse> notifications(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        engineOrchestrator.runNotifications(asOfDate);
        LocalDate resolved = engineOrchestrator.resolveAsOfDate(asOfDate);
        return ResponseEntity.ok(new EngineRunResponse(resolved.toString(), "OK"));
    }
}
