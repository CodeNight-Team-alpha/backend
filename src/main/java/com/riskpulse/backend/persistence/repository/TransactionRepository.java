package com.riskpulse.backend.persistence.repository;

import com.riskpulse.backend.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

    List<TransactionEntity> findTop5ByOrderByTransactionDateDesc();

    @Query("SELECT MAX(t.transactionDate) FROM TransactionEntity t")
    Optional<LocalDate> findMaxTransactionDate();

    /** Tüm işlem günlerini (distinct transaction_date) tarih sırasına göre döner. Challenge motorunun her gün için çalışması için kullanılır. */
    @Query("SELECT DISTINCT t.transactionDate FROM TransactionEntity t ORDER BY t.transactionDate")
    List<LocalDate> findDistinctTransactionDatesAsc();
}
