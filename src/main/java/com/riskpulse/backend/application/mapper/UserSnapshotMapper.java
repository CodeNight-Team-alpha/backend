package com.riskpulse.backend.application.mapper;

import com.riskpulse.backend.api.dto.UserSnapshotResponse;
import com.riskpulse.backend.persistence.entity.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Maps persistence entities to UserSnapshotResponse DTO.
 */
public final class UserSnapshotMapper {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private UserSnapshotMapper() {}

    public static UserSnapshotResponse toResponse(
            String userId,
            String asOfDate,
            Optional<UserStateEntity> userState,
            Optional<ChallengeAwardEntity> challengeAward,
            BigDecimal totalPoints,
            List<BadgeAwardEntity> badgeAwards,
            List<BadgeEntity> badgesById,
            List<NotificationEntity> notifications) {

        UserSnapshotResponse.MetricsDto metrics = userState
                .map(s -> new UserSnapshotResponse.MetricsDto(
                        s.getSpendToday(),
                        s.getUniqueCategoriesToday(),
                        s.getElectronicsSpendToday(),
                        s.getSpend7d()))
                .orElse(null);

        UserSnapshotResponse.ChallengeDto challenge = challengeAward
                .map(a -> new UserSnapshotResponse.ChallengeDto(
                        a.getAwardId(),
                        a.getChallengeId(),
                        a.getRewardPoints()))
                .orElse(null);

        UserSnapshotResponse.PointsDto points = new UserSnapshotResponse.PointsDto(
                totalPoints != null ? totalPoints : BigDecimal.ZERO);

        List<UserSnapshotResponse.BadgeDto> badgeDtos = new ArrayList<>();
        for (BadgeAwardEntity award : badgeAwards) {
            String bid = award.getId() != null ? award.getId().getBadgeId() : null;
            BadgeEntity badge = badgesById.stream().filter(b -> bid != null && bid.equals(b.getBadgeId())).findFirst().orElse(null);
            badgeDtos.add(new UserSnapshotResponse.BadgeDto(
                    bid,
                    badge != null ? badge.getBadgeName() : null,
                    badge != null ? badge.getThresholdPoints() : null));
        }

        List<UserSnapshotResponse.NotificationDto> notifDtos = notifications.stream()
                .map(n -> new UserSnapshotResponse.NotificationDto(
                        n.getId(),
                        n.getSourceRef(),
                        n.getMessage(),
                        n.getCreatedAt() != null ? n.getCreatedAt().format(ISO) : null))
                .toList();

        return new UserSnapshotResponse(
                userId,
                asOfDate,
                metrics,
                challenge,
                points,
                badgeDtos,
                notifDtos);
    }
}
