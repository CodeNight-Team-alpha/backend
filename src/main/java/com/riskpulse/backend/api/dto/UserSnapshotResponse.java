package com.riskpulse.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserSnapshotResponse(
        String userId,
        String asOfDate,
        MetricsDto metrics,
        ChallengeDto challenge,
        PointsDto points,
        List<BadgeDto> badges,
        List<NotificationDto> notifications
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MetricsDto(
            BigDecimal spendToday,
            Integer uniqueCategoriesToday,
            BigDecimal electronicsSpendToday,
            BigDecimal spend7d
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChallengeDto(
            String awardId,
            String challengeId,
            Integer rewardPoints
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PointsDto(BigDecimal totalPoints) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record BadgeDto(String badgeId, String badgeName, Integer threshold) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record NotificationDto(Long id, String sourceRef, String message, String createdAt) {}
}
