package com.riskpulse.backend.api.controller;

import com.riskpulse.backend.api.dto.LeaderboardResponse;
import com.riskpulse.backend.application.service.LeaderboardQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Thin controller: optional asOfDate, single query service call, response.
 */
@RestController
@RequestMapping("/api/v1/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardQueryService leaderboardQueryService;

    @GetMapping
    public ResponseEntity<LeaderboardResponse> get(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        LeaderboardResponse body = leaderboardQueryService.getLeaderboard(asOfDate);
        return ResponseEntity.ok(body);
    }
}
