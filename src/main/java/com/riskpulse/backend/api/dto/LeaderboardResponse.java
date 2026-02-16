package com.riskpulse.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LeaderboardResponse(
        String asOfDate,
        List<LeaderboardEntryDto> top
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record LeaderboardEntryDto(
            int rank,
            String userId,
            String displayName,
            BigDecimal totalPoints
    ) {}
}
