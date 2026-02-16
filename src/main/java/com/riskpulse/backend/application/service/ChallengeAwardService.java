package com.riskpulse.backend.application.service;

import com.riskpulse.backend.domain.model.ChallengeDecision;
import com.riskpulse.backend.domain.model.UserState;
import com.riskpulse.backend.domain.service.ChallengeEngine;
import com.riskpulse.backend.domain.model.ChallengeRule;
import com.riskpulse.backend.persistence.entity.ChallengeAwardEntity;
import com.riskpulse.backend.persistence.entity.ChallengeEntity;
import com.riskpulse.backend.persistence.entity.UserStateEntity;
import com.riskpulse.backend.persistence.repository.ChallengeAwardRepository;
import com.riskpulse.backend.persistence.repository.ChallengeRepository;
import com.riskpulse.backend.persistence.repository.UserStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeAwardService {

    private static final Logger log = LoggerFactory.getLogger(ChallengeAwardService.class);

    private final UserStateRepository userStateRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeAwardRepository challengeAwardRepository;

    public ChallengeAwardService(UserStateRepository userStateRepository,
                                 ChallengeRepository challengeRepository,
                                 ChallengeAwardRepository challengeAwardRepository) {
        this.userStateRepository = userStateRepository;
        this.challengeRepository = challengeRepository;
        this.challengeAwardRepository = challengeAwardRepository;
    }

    @Transactional
    public int evaluateAndUpsertAwards(LocalDate asOfDate) {
        List<UserStateEntity> stateRows = userStateRepository.findById_AsOfDate(asOfDate);
        List<ChallengeEntity> activeChallenges = challengeRepository.findByIsActiveTrueOrderByPriorityAsc();
        List<ChallengeRule> rules = activeChallenges.stream()
                .map(c -> new ChallengeRule(c.getChallengeId(), c.getRewardPoints(), c.getPriority()))
                .collect(Collectors.toList());

        int rows = 0;
        for (UserStateEntity row : stateRows) {
            UserState state = toUserState(row);
            ChallengeDecision decision = ChallengeEngine.evaluate(state, rules);
            String userId = row.getId().getUserId();
            ChallengeAwardEntity entity = toAwardEntity(userId, asOfDate, decision);
            ChallengeAwardEntity existing = challengeAwardRepository.findByUserIdAndAsOfDate(userId, asOfDate).orElse(null);
            if (existing != null) {
                entity.setAwardId(existing.getAwardId());
                entity.setCreatedAt(existing.getCreatedAt());
            }
            challengeAwardRepository.save(entity);
            rows++;
        }
        log.info("challenge_awards upsert done for asOfDate={} rows={}", asOfDate, rows);
        return rows;
    }

    private static UserState toUserState(UserStateEntity row) {
        return UserState.fromMetrics(
                row.getSpendToday(),
                row.getUniqueCategoriesToday(),
                row.getElectronicsSpendToday(),
                row.getSpend7d());
    }

    private static ChallengeAwardEntity toAwardEntity(String userId, LocalDate asOfDate, ChallengeDecision decision) {
        String awardId = "AW-" + userId + "-" + asOfDate;
        return ChallengeAwardEntity.builder()
                .awardId(awardId)
                .userId(userId)
                .asOfDate(asOfDate)
                .challengeId(decision.getSelectedOptional().orElse(null))
                .selectedChallenge(decision.getSelectedOptional().orElse(null))
                .triggeredChallenges(toJsonArray(decision.getTriggered()))
                .suppressedChallenges(toJsonArray(decision.getSuppressed()))
                .rewardPoints(decision.getRewardPoints())
                .createdAt(OffsetDateTime.now())
                .build();
    }

    private static String toJsonArray(List<String> ids) {
        if (ids == null || ids.isEmpty()) return "[]";
        return "[\"" + String.join("\",\"", ids) + "\"]";
    }
}
