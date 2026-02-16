package com.riskpulse.backend.application.mapper;

import com.riskpulse.backend.api.dto.ChallengeAwardDetailDto;
import com.riskpulse.backend.api.dto.UserSnapshotResponse;
import com.riskpulse.backend.persistence.entity.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                        a.getRewardPoints(),
                        parseChallengeIdList(a.getTriggeredChallenges()),
                        parseChallengeIdList(a.getSuppressedChallenges())))
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

    /** Parses stored JSON array string e.g. ["C-04","C-03"] to list of challenge ids. */
    static List<String> parseChallengeIdList(String json) {
        if (json == null || json.isBlank() || "[]".equals(json.trim())) {
            return List.of();
        }
        String inner = json.replace("[", "").replace("]", "").replace("\"", "").trim();
        if (inner.isEmpty()) return List.of();
        return Arrays.stream(inner.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public static ChallengeAwardDetailDto toChallengeAwardDetailDto(ChallengeAwardEntity a) {
        if (a == null) return null;
        return new ChallengeAwardDetailDto(
                a.getAwardId(),
                a.getAsOfDate() != null ? a.getAsOfDate().toString() : null,
                parseChallengeIdList(a.getTriggeredChallenges()),
                a.getSelectedChallenge() != null ? a.getSelectedChallenge() : a.getChallengeId(),
                parseChallengeIdList(a.getSuppressedChallenges()),
                a.getRewardPoints()
        );
    }
}
