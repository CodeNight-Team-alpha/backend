package com.riskpulse.backend.application.mapper;

import com.riskpulse.backend.api.dto.LeaderboardResponse;
import com.riskpulse.backend.persistence.projection.LeaderboardRowProjection;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Maps leaderboard projections to LeaderboardResponse DTO.
 */
public final class LeaderboardMapper {

    private LeaderboardMapper() {}

    public static LeaderboardResponse toResponse(String asOfDate, List<LeaderboardRowProjection> rows,
                                                 Map<String, List<LeaderboardResponse.BadgeInfo>> badgesByUserId) {
        List<LeaderboardResponse.LeaderboardEntryDto> top = rows.stream()
                .map(r -> new LeaderboardResponse.LeaderboardEntryDto(
                        r.getRank(),
                        r.getUserId(),
                        r.getDisplayName(),
                        r.getTotalPoints(),
                        badgesByUserId.getOrDefault(r.getUserId(), Collections.emptyList())))
                .collect(Collectors.toList());
        return new LeaderboardResponse(asOfDate, top);
    }
}
