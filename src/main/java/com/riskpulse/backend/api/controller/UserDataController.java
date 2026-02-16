package com.riskpulse.backend.api.controller;

import com.riskpulse.backend.api.dto.ChallengeAwardDetailDto;
import com.riskpulse.backend.api.dto.NotificationItemDto;
import com.riskpulse.backend.api.dto.UserSnapshotResponse;
import com.riskpulse.backend.application.service.UserNotificationsQueryService;
import com.riskpulse.backend.application.service.UserSnapshotQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Thin controller: path params + optional asOfDate, single query service call, response.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserDataController {

    private final UserSnapshotQueryService snapshotQueryService;
    private final UserNotificationsQueryService notificationsQueryService;

    @GetMapping("/{userId}/snapshot")
    public ResponseEntity<UserSnapshotResponse> snapshot(
            @PathVariable String userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        UserSnapshotResponse body = snapshotQueryService.getSnapshot(userId, asOfDate);
        return ResponseEntity.ok(body);
    }

    /** Kullanıcının tüm challenge ödülleri: tetiklenen, seçilen ve bastırılan görevler (tarih azalan). */
    @GetMapping("/{userId}/challenge-awards")
    public ResponseEntity<List<ChallengeAwardDetailDto>> challengeAwards(@PathVariable String userId) {
        List<ChallengeAwardDetailDto> body = snapshotQueryService.getChallengeAwards(userId);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{userId}/notifications")
    public ResponseEntity<List<NotificationItemDto>> notifications(
            @PathVariable String userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        List<NotificationItemDto> body = notificationsQueryService.getNotifications(userId);
        return ResponseEntity.ok(body);
    }
}
