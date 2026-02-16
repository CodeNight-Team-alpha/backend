package com.riskpulse.backend.application.service;

import com.riskpulse.backend.persistence.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Application-layer orchestrator: runs engine steps in order and resolves asOfDate.
 */
@Service
public class EngineOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(EngineOrchestrator.class);

    private final TransactionRepository transactionRepository;
    private final MetricsService metricsService;
    private final ChallengeAwardService challengeAwardService;
    private final PointsLedgerService pointsLedgerService;
    private final BadgeService badgeService;
    private final LeaderboardService leaderboardService;
    private final NotificationService notificationService;

    public EngineOrchestrator(TransactionRepository transactionRepository,
                              MetricsService metricsService,
                              ChallengeAwardService challengeAwardService,
                              PointsLedgerService pointsLedgerService,
                              BadgeService badgeService,
                              LeaderboardService leaderboardService,
                              NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.metricsService = metricsService;
        this.challengeAwardService = challengeAwardService;
        this.pointsLedgerService = pointsLedgerService;
        this.badgeService = badgeService;
        this.leaderboardService = leaderboardService;
        this.notificationService = notificationService;
    }

    /**
     * Resolves asOfDate: if null, uses MAX(transaction_date) from DB; if still null, uses today.
     */
    public LocalDate resolveAsOfDate(LocalDate asOfDate) {
        if (asOfDate != null) {
            return asOfDate;
        }
        Optional<LocalDate> max = transactionRepository.findMaxTransactionDate();
        LocalDate resolved = max.orElse(LocalDate.now());
        log.debug("resolved asOfDate: requested=null -> {}", resolved);
        return resolved;
    }

    public void runAll(LocalDate asOfDate) {
        LocalDate date = resolveAsOfDate(asOfDate);
        runMetrics(date);
        runChallenges(date);
        runPoints(date);
        runBadges(date);
        runLeaderboard(date);
        runNotifications(date);
    }

    /**
     * Kullanıcının işlem yaptığı tüm günler için motoru çalıştırır (metrics → challenges → points → badges → leaderboard → notifications).
     * Böylece her transaction_date için challenge ödülü ve puan üretilir.
     */
    public void runAllForAllTransactionDates() {
        List<LocalDate> dates = transactionRepository.findDistinctTransactionDatesAsc();
        if (dates.isEmpty()) {
            log.debug("no transaction dates to process");
            return;
        }
        log.info("running engine for {} transaction date(s): {} ... {}", dates.size(), dates.get(0), dates.get(dates.size() - 1));
        for (LocalDate d : dates) {
            runAll(d);
        }
    }

    public void runMetrics(LocalDate asOfDate) {
        LocalDate date = resolveAsOfDate(asOfDate);
        metricsService.computeAndUpsertUserState(date);
    }

    public void runChallenges(LocalDate asOfDate) {
        LocalDate date = resolveAsOfDate(asOfDate);
        challengeAwardService.evaluateAndUpsertAwards(date);
    }

    public void runPoints(LocalDate asOfDate) {
        LocalDate date = resolveAsOfDate(asOfDate);
        pointsLedgerService.appendChallengePoints(date);
    }

    public void runBadges(LocalDate asOfDate) {
        LocalDate date = resolveAsOfDate(asOfDate);
        badgeService.evaluateAndAwardBadges(date);
    }

    public void runLeaderboard(LocalDate asOfDate) {
        LocalDate date = resolveAsOfDate(asOfDate);
        leaderboardService.computeAndUpsertLeaderboard(date);
    }

    public void runNotifications(LocalDate asOfDate) {
        LocalDate date = resolveAsOfDate(asOfDate);
        notificationService.generateNotifications(date);
    }
}
