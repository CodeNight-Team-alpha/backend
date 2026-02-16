package com.riskpulse.backend.application.service;

import com.riskpulse.backend.api.dto.LeaderboardResponse;
import com.riskpulse.backend.application.mapper.LeaderboardMapper;
import com.riskpulse.backend.persistence.projection.LeaderboardRowProjection;
import com.riskpulse.backend.persistence.repository.LeaderboardRepository;
import com.riskpulse.backend.persistence.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Application service: loads leaderboard from repo and uses mapper to build DTO.
 */
@Service
public class LeaderboardQueryService {

    private final LeaderboardRepository leaderboardRepository;
    private final TransactionRepository transactionRepository;

    public LeaderboardQueryService(LeaderboardRepository leaderboardRepository,
                                   TransactionRepository transactionRepository) {
        this.leaderboardRepository = leaderboardRepository;
        this.transactionRepository = transactionRepository;
    }

    public LeaderboardResponse getLeaderboard(LocalDate asOfDate) {
        LocalDate date = asOfDate != null ? asOfDate : transactionRepository.findMaxTransactionDate().orElse(LocalDate.now());
        List<LeaderboardRowProjection> rows = leaderboardRepository.findRowsByAsOfDate(date);
        return LeaderboardMapper.toResponse(date.toString(), rows);
    }
}
