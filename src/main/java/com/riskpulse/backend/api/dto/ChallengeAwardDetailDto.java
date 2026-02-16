package com.riskpulse.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChallengeAwardDetailDto(
        String awardId,
        String asOfDate,
        List<String> triggeredChallengeIds,
        String selectedChallengeId,
        List<String> suppressedChallengeIds,
        Integer rewardPoints
) {}
