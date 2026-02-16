package com.riskpulse.backend.application.service;

import com.riskpulse.backend.persistence.entity.ChallengeAwardEntity;
import com.riskpulse.backend.persistence.entity.NotificationEntity;
import com.riskpulse.backend.persistence.repository.ChallengeAwardRepository;
import com.riskpulse.backend.persistence.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * Challenge ödülü verildiğinde kullanıcıya bildirim üretir (FR-15). Idempotent: (user_id, source_ref) unique.
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    /** Challenge id → Türkçe gösterim adı (tablodaki challenge_name ile uyumlu). */
    private static final Map<String, String> CHALLENGE_NAME_TR = Map.of(
            "C-01", "Günlük Harcama",
            "C-02", "Kategori Avcısı",
            "C-03", "Elektronik Bonus",
            "C-04", "Haftalık Aktif"
    );

    private final ChallengeAwardRepository challengeAwardRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public int generateNotifications(LocalDate asOfDate) {
        List<ChallengeAwardEntity> awards = challengeAwardRepository.findByAsOfDate(asOfDate);
        int inserted = 0;
        for (ChallengeAwardEntity award : awards) {
            String userId = award.getUserId();
            String sourceRef = award.getAwardId();
            if (notificationRepository.existsByUserIdAndSourceRef(userId, sourceRef)) {
                continue;
            }
            Integer points = award.getRewardPoints() != null ? award.getRewardPoints() : 0;
            String message = buildNotificationMessage(award.getChallengeId(), points);
            NotificationEntity notification = NotificationEntity.builder()
                    .userId(userId)
                    .sourceRef(sourceRef)
                    .message(message)
                    .completedAt(award.getAsOfDate())
                    .createdAt(OffsetDateTime.now())
                    .build();
            notificationRepository.save(notification);
            inserted++;
        }
        log.info("notifications generated for asOfDate={} inserted={}", asOfDate, inserted);
        return inserted;
    }

    private String buildNotificationMessage(String challengeId, int points) {
        if (points == 0 || challengeId == null || challengeId.isBlank()) {
            return "Bu gün tamamlanan görev yok.";
        }
        String challengeName = CHALLENGE_NAME_TR.getOrDefault(challengeId, "Görev");
        return String.format("%s tamamlandı: +%d puan", challengeName, points);
    }
}
