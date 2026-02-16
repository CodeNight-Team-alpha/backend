package com.riskpulse.backend.application.service;

import com.riskpulse.backend.domain.model.Badge;
import com.riskpulse.backend.domain.service.BadgeEngine;
import com.riskpulse.backend.persistence.entity.BadgeAwardEntity;
import com.riskpulse.backend.persistence.entity.BadgeAwardId;
import com.riskpulse.backend.persistence.entity.BadgeEntity;
import com.riskpulse.backend.persistence.entity.UserEntity;
import com.riskpulse.backend.persistence.repository.BadgeAwardRepository;
import com.riskpulse.backend.persistence.repository.BadgeRepository;
import com.riskpulse.backend.persistence.repository.PointsLedgerRepository;
import com.riskpulse.backend.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BadgeService {

    private static final Logger log = LoggerFactory.getLogger(BadgeService.class);

    private final PointsLedgerRepository pointsLedgerRepository;
    private final BadgeRepository badgeRepository;
    private final BadgeAwardRepository badgeAwardRepository;
    private final UserRepository userRepository;

    public BadgeService(PointsLedgerRepository pointsLedgerRepository,
                        BadgeRepository badgeRepository,
                        BadgeAwardRepository badgeAwardRepository,
                        UserRepository userRepository) {
        this.pointsLedgerRepository = pointsLedgerRepository;
        this.badgeRepository = badgeRepository;
        this.badgeAwardRepository = badgeAwardRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AwardResult evaluateAndAwardBadges(LocalDate asOfDate) {
        List<Badge> badges = toDomainBadges(badgeRepository.findAllByOrderByThresholdPointsAsc());
        List<String> userIds = userRepository.findAll().stream()
                .map(UserEntity::getUserId)
                .sorted()
                .toList();

        int inserted = 0;
        int skipped = 0;
        for (String userId : userIds) {
            BigDecimal total = pointsLedgerRepository.sumPointsByUserId(userId);
            int totalPoints = total != null ? total.intValue() : 0;
            List<Badge> earned = BadgeEngine.earnedBadges(totalPoints, badges);

            for (Badge badge : earned) {
                if (badgeAwardRepository.existsByUserIdAndBadgeId(userId, badge.getBadgeId())) {
                    skipped++;
                    continue;
                }
                BadgeAwardEntity award = toAwardEntity(userId, badge);
                badgeAwardRepository.save(award);
                inserted++;
            }
        }
        log.info("badge_awards done: inserted={} skipped={}", inserted, skipped);
        return new AwardResult(inserted, skipped);
    }

    private static List<Badge> toDomainBadges(List<BadgeEntity> entities) {
        return entities.stream()
                .map(e -> new Badge(e.getBadgeId(), e.getBadgeName(), e.getThresholdPoints()))
                .collect(Collectors.toList());
    }

    private static BadgeAwardEntity toAwardEntity(String userId, Badge badge) {
        return BadgeAwardEntity.builder()
                .id(BadgeAwardId.builder().userId(userId).badgeId(badge.getBadgeId()).build())
                .awardId("BA-" + userId + "-" + badge.getBadgeId())
                .awardedAt(OffsetDateTime.now())
                .build();
    }

    public record AwardResult(int inserted, int skipped) {}
}
