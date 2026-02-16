package com.riskpulse.backend.application.runner;

import com.riskpulse.backend.application.service.EngineOrchestrator;
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
    private final EngineOrchestrator engineOrchestrator;
    private final PointsLedgerService pointsLedgerService;

    public DbSanityCheck(UserRepository userRepository,
                         MerchantRepository merchantRepository,
                         TransactionRepository transactionRepository,
                         EngineOrchestrator engineOrchestrator,
                         PointsLedgerService pointsLedgerService) {
        this.userRepository = userRepository;
        this.merchantRepository = merchantRepository;
        this.transactionRepository = transactionRepository;
        this.engineOrchestrator = engineOrchestrator;
        this.pointsLedgerService = pointsLedgerService;
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

        // Kullanıcının işlem yaptığı tüm günler için motoru çalıştır (her gün için challenge + puan üretimi)
        engineOrchestrator.runAllForAllTransactionDates();
        pointsLedgerService.logTotalPointsByUser();
    }
}
