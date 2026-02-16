package com.riskpulse.backend.application.service;

import com.riskpulse.backend.persistence.entity.ChallengeAwardEntity;
import com.riskpulse.backend.persistence.entity.PointsLedgerEntity;
import com.riskpulse.backend.persistence.repository.ChallengeAwardRepository;
import com.riskpulse.backend.persistence.repository.PointsLedgerRepository;
import com.riskpulse.backend.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointsLedgerService {

    private static final Logger log = LoggerFactory.getLogger(PointsLedgerService.class);
    private static final String SOURCE_CHALLENGE = "challenge";

    private final ChallengeAwardRepository challengeAwardRepository;
    private final PointsLedgerRepository pointsLedgerRepository;
    private final UserRepository userRepository;

    @Transactional
    public AppendResult appendChallengePoints(LocalDate asOfDate) {
        List<ChallengeAwardEntity> awards = challengeAwardRepository.findByAsOfDate(asOfDate);
        int inserted = 0;
        int skipped = 0;
        for (ChallengeAwardEntity award : awards) {
            String awardId = award.getAwardId();
            if (pointsLedgerRepository.existsBySourceAndSourceRef(SOURCE_CHALLENGE, awardId)) {
                skipped++;
                continue;
            }
            int points = award.getRewardPoints() != null ? award.getRewardPoints() : 0;
            PointsLedgerEntity row = PointsLedgerEntity.builder()
                    .userId(award.getUserId())
                    .pointsDelta(BigDecimal.valueOf(points))
                    .source(SOURCE_CHALLENGE)
                    .sourceRef(awardId)
                    .createdAt(OffsetDateTime.now())
                    .build();
            pointsLedgerRepository.save(row);
            inserted++;
        }
        log.info("points_ledger append done for asOfDate={} inserted={} skipped={}", asOfDate, inserted, skipped);
        return new AppendResult(inserted, skipped);
    }

    public void logTotalPointsByUser() {
        List<String> userIds = userRepository.findAll().stream()
                .map(u -> u.getUserId())
                .sorted()
                .toList();
        StringBuilder sb = new StringBuilder("totalPoints");
        for (String userId : userIds) {
            BigDecimal total = pointsLedgerRepository.sumPointsByUserId(userId);
            sb.append(" ").append(userId).append("=").append(total != null ? total.intValue() : 0);
        }
        log.info(sb.toString());
    }

    public record AppendResult(int inserted, int skipped) {}
}
