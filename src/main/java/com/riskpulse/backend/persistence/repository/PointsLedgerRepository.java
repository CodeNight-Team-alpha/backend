package com.riskpulse.backend.persistence.repository;

import com.riskpulse.backend.persistence.entity.PointsLedgerEntity;
import com.riskpulse.backend.persistence.projection.UserPointsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public interface PointsLedgerRepository extends JpaRepository<PointsLedgerEntity, Long> {

    boolean existsBySourceAndSourceRef(String source, String sourceRef);

    @Query("SELECT COALESCE(SUM(p.pointsDelta), 0) FROM PointsLedgerEntity p WHERE p.userId = :userId")
    BigDecimal sumPointsByUserId(@Param("userId") String userId);

    @Query("SELECT p.userId AS userId, SUM(p.pointsDelta) AS totalPoints FROM PointsLedgerEntity p WHERE p.createdAt <= :endOfDay GROUP BY p.userId ORDER BY SUM(p.pointsDelta) DESC, p.userId ASC")
    List<UserPointsProjection> findTotalPointsByUserAsOf(@Param("endOfDay") OffsetDateTime endOfDay);
}
