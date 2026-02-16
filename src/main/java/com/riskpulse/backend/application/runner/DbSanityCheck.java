package com.riskpulse.backend.application.runner;

import com.riskpulse.backend.application.service.BadgeService;
import com.riskpulse.backend.application.service.ChallengeAwardService;
import com.riskpulse.backend.application.service.MetricsService;
import com.riskpulse.backend.application.service.PointsLedgerService;
import com.riskpulse.backend.persistence.entity.TransactionEntity;
import com.riskpulse.backend.persistence.repository.MerchantRepository;
import com.riskpulse.backend.persistence.repository.TransactionRepository;
import com.riskpulse.backend.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbSanityCheck implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DbSanityCheck.class);

    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;
    private final TransactionRepository transactionRepository;
    private final MetricsService metricsService;
    private final ChallengeAwardService challengeAwardService;
    private final PointsLedgerService pointsLedgerService;
    private final BadgeService badgeService;

    public DbSanityCheck(UserRepository userRepository,
                         MerchantRepository merchantRepository,
                         TransactionRepository transactionRepository,
                         MetricsService metricsService,
                         ChallengeAwardService challengeAwardService,
                         PointsLedgerService pointsLedgerService,
                         BadgeService badgeService) {
        this.userRepository = userRepository;
        this.merchantRepository = merchantRepository;
        this.transactionRepository = transactionRepository;
        this.metricsService = metricsService;
        this.challengeAwardService = challengeAwardService;
        this.pointsLedgerService = pointsLedgerService;
        this.badgeService = badgeService;
    }

    @Override
    public void run(ApplicationArguments args) {
        long users = userRepository.count();
        long merchants = merchantRepository.count();
        long transactions = transactionRepository.count();
        log.info("DB OK - users={}, merchants={}, transactions={}", users, merchants, transactions);

        List<TransactionEntity> recent = transactionRepository.findTop5ByOrderByTransactionDateDesc();
        if (!recent.isEmpty()) {
            TransactionEntity t = recent.get(0);
            log.info("Sample transaction: id={}, date={}", t.getTransactionId(), t.getTransactionDate());
        }

        transactionRepository.findMaxTransactionDate().ifPresent(maxDate -> {
            int rows = metricsService.computeAndUpsertUserState(maxDate);
            log.info("user_state upsert done for asOfDate={} rows={}", maxDate, rows);
            int awardRows = challengeAwardService.evaluateAndUpsertAwards(maxDate);
            log.info("challenge_awards upsert done for asOfDate={} rows={}", maxDate, awardRows);
            pointsLedgerService.appendChallengePoints(maxDate);
            pointsLedgerService.logTotalPointsByUser();
            badgeService.evaluateAndAwardBadges(maxDate);
        });
    }
}
