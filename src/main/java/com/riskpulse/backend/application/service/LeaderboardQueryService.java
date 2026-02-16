package com.riskpulse.backend.application.service;

import com.riskpulse.backend.api.dto.LeaderboardResponse;
import com.riskpulse.backend.application.mapper.LeaderboardMapper;
import com.riskpulse.backend.persistence.entity.BadgeAwardEntity;
import com.riskpulse.backend.persistence.entity.BadgeEntity;
import com.riskpulse.backend.persistence.projection.LeaderboardRowProjection;
import com.riskpulse.backend.persistence.repository.BadgeAwardRepository;
import com.riskpulse.backend.persistence.repository.BadgeRepository;
import com.riskpulse.backend.persistence.repository.LeaderboardRepository;
import com.riskpulse.backend.persistence.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Application service: loads leaderboard from repo and uses mapper to build DTO.
 */
@Service
public class LeaderboardQueryService {

    private final LeaderboardRepository leaderboardRepository;
    private final TransactionRepository transactionRepository;
    private final BadgeAwardRepository badgeAwardRepository;
    private final BadgeRepository badgeRepository;

    public LeaderboardQueryService(LeaderboardRepository leaderboardRepository,
                                   TransactionRepository transactionRepository,
                                   BadgeAwardRepository badgeAwardRepository,
                                   BadgeRepository badgeRepository) {
        this.leaderboardRepository = leaderboardRepository;
        this.transactionRepository = transactionRepository;
        this.badgeAwardRepository = badgeAwardRepository;
        this.badgeRepository = badgeRepository;
    }

    public LeaderboardResponse getLeaderboard(LocalDate asOfDate) {
        LocalDate date = asOfDate != null ? asOfDate : transactionRepository.findMaxTransactionDate().orElse(LocalDate.now());
        List<LeaderboardRowProjection> rows = leaderboardRepository.findRowsByAsOfDate(date);
        List<String> userIds = rows.stream().map(LeaderboardRowProjection::getUserId).toList();
        List<BadgeAwardEntity> awardEntities = userIds.isEmpty() ? List.of() : badgeAwardRepository.findById_UserIdIn(userIds);
        List<String> badgeIds = awardEntities.stream()
                .map(a -> a.getId() != null ? a.getId().getBadgeId() : null)
                .filter(id -> id != null)
                .distinct()
                .toList();
        Map<String, BadgeEntity> badgesById = badgeIds.isEmpty() ? Map.of() : badgeRepository.findAllById(badgeIds).stream().collect(Collectors.toMap(BadgeEntity::getBadgeId, b -> b));
        Map<String, List<LeaderboardResponse.BadgeInfo>> badgesByUserId = new java.util.HashMap<>();
        for (BadgeAwardEntity a : awardEntities) {
            if (a.getId() == null) continue;
            String uid = a.getId().getUserId();
            String bid = a.getId().getBadgeId();
            BadgeEntity badge = badgesById.get(bid);
            badgesByUserId.computeIfAbsent(uid, k -> new ArrayList<>()).add(
                    new LeaderboardResponse.BadgeInfo(
                            bid,
                            badge != null ? badge.getBadgeName() : null,
                            badge != null ? badge.getThresholdPoints() : null));
        }
        return LeaderboardMapper.toResponse(date.toString(), rows, badgesByUserId);
    }
}
