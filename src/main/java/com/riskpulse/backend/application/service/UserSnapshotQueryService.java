package com.riskpulse.backend.application.service;

import com.riskpulse.backend.api.dto.UserSnapshotResponse;
import com.riskpulse.backend.application.mapper.UserSnapshotMapper;
import com.riskpulse.backend.persistence.entity.*;
import com.riskpulse.backend.persistence.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Application service: loads snapshot data from repos and uses mapper to build DTO.
 */
@Service
public class UserSnapshotQueryService {

    private final UserStateRepository userStateRepository;
    private final ChallengeAwardRepository challengeAwardRepository;
    private final PointsLedgerRepository pointsLedgerRepository;
    private final BadgeAwardRepository badgeAwardRepository;
    private final BadgeRepository badgeRepository;
    private final NotificationRepository notificationRepository;
    private final TransactionRepository transactionRepository;

    public UserSnapshotQueryService(UserStateRepository userStateRepository,
                                   ChallengeAwardRepository challengeAwardRepository,
                                   PointsLedgerRepository pointsLedgerRepository,
                                   BadgeAwardRepository badgeAwardRepository,
                                   BadgeRepository badgeRepository,
                                   NotificationRepository notificationRepository,
                                   TransactionRepository transactionRepository) {
        this.userStateRepository = userStateRepository;
        this.challengeAwardRepository = challengeAwardRepository;
        this.pointsLedgerRepository = pointsLedgerRepository;
        this.badgeAwardRepository = badgeAwardRepository;
        this.badgeRepository = badgeRepository;
        this.notificationRepository = notificationRepository;
        this.transactionRepository = transactionRepository;
    }

    public UserSnapshotResponse getSnapshot(String userId, LocalDate asOfDate) {
        LocalDate date = asOfDate != null ? asOfDate : transactionRepository.findMaxTransactionDate().orElse(LocalDate.now());

        Optional<UserStateEntity> userState = userStateRepository.findById(UserStateId.builder().userId(userId).asOfDate(date).build());
        Optional<ChallengeAwardEntity> challengeAward = challengeAwardRepository.findByUserIdAndAsOfDate(userId, date);
        BigDecimal totalPoints = pointsLedgerRepository.sumPointsByUserId(userId);
        List<BadgeAwardEntity> badgeAwards = badgeAwardRepository.findById_UserId(userId);
        List<String> badgeIds = badgeAwards.stream()
                .map(a -> a.getId() != null ? a.getId().getBadgeId() : null)
                .filter(id -> id != null)
                .distinct()
                .toList();
        List<BadgeEntity> badges = badgeIds.isEmpty() ? List.of() : badgeRepository.findAllById(badgeIds);
        List<NotificationEntity> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return UserSnapshotMapper.toResponse(
                userId,
                date.toString(),
                userState,
                challengeAward,
                totalPoints,
                badgeAwards,
                badges,
                notifications);
    }
}
