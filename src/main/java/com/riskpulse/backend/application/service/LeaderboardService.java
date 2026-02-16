package com.riskpulse.backend.application.service;

import com.riskpulse.backend.domain.model.RankedEntry;
import com.riskpulse.backend.domain.model.UserPoints;
import com.riskpulse.backend.domain.service.LeaderboardRanking;
import com.riskpulse.backend.persistence.entity.LeaderboardEntity;
import com.riskpulse.backend.persistence.entity.LeaderboardId;
import com.riskpulse.backend.persistence.projection.UserPointsProjection;
import com.riskpulse.backend.persistence.repository.LeaderboardRepository;
import com.riskpulse.backend.persistence.repository.PointsLedgerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    private static final Logger log = LoggerFactory.getLogger(LeaderboardService.class);

    private final PointsLedgerRepository pointsLedgerRepository;
    private final LeaderboardRepository leaderboardRepository;

    public LeaderboardService(PointsLedgerRepository pointsLedgerRepository,
                              LeaderboardRepository leaderboardRepository) {
        this.pointsLedgerRepository = pointsLedgerRepository;
        this.leaderboardRepository = leaderboardRepository;
    }

    @Transactional
    public int computeAndUpsertLeaderboard(LocalDate asOfDate) {
        OffsetDateTime endOfDay = asOfDate.atTime(23, 59, 59, 999_000_000).atOffset(ZoneOffset.UTC);
        List<UserPointsProjection> rows = pointsLedgerRepository.findTotalPointsByUserAsOf(endOfDay);
        List<UserPoints> userPoints = rows.stream()
                .map(r -> new UserPoints(r.getUserId(), r.getTotalPoints()))
                .collect(Collectors.toList());

        List<RankedEntry> ranked = LeaderboardRanking.assignRanks(userPoints);

        leaderboardRepository.deleteById_AsOfDate(asOfDate);
        for (RankedEntry entry : ranked) {
            LeaderboardEntity entity = toEntity(asOfDate, entry);
            leaderboardRepository.save(entity);
        }
        log.info("leaderboard computed for asOfDate={} rows={}", asOfDate, ranked.size());
        return ranked.size();
    }

    private static LeaderboardEntity toEntity(LocalDate asOfDate, RankedEntry entry) {
        return LeaderboardEntity.builder()
                .id(LeaderboardId.builder().asOfDate(asOfDate).userId(entry.getUserId()).build())
                .rank(entry.getRank())
                .totalPoints(entry.getTotalPoints())
                .createdAt(OffsetDateTime.now())
                .build();
    }
}
