package com.riskpulse.backend.persistence.repository;

import com.riskpulse.backend.persistence.entity.UserStateEntity;
import com.riskpulse.backend.persistence.entity.UserStateId;
import com.riskpulse.backend.persistence.projection.UserStateMetricsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserStateMetricsRepository extends JpaRepository<UserStateEntity, UserStateId> {

    @Query(value = """
        SELECT t.user_id AS userId,
          SUM(CASE WHEN t.transaction_date = :asOfDate THEN t.amount ELSE 0 END) AS spendToday,
          COUNT(DISTINCT CASE WHEN t.transaction_date = :asOfDate THEN m.category END) AS uniqueCategoriesToday,
          SUM(CASE WHEN t.transaction_date = :asOfDate AND m.category = 'ELECTRONICS' THEN t.amount ELSE 0 END) AS electronicsSpendToday,
          SUM(t.amount) AS spend7d
        FROM transactions t
        JOIN merchants m ON t.merchant_id = m.merchant_id
        WHERE t.transaction_date BETWEEN :startDate AND :asOfDate
        GROUP BY t.user_id
        """, nativeQuery = true)
    List<UserStateMetricsProjection> findMetricsByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("asOfDate") LocalDate asOfDate);
}
