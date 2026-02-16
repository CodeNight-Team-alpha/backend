package com.riskpulse.backend.persistence.repository;

import com.riskpulse.backend.persistence.entity.LeaderboardEntity;
import com.riskpulse.backend.persistence.entity.LeaderboardId;
import com.riskpulse.backend.persistence.projection.LeaderboardRowProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaderboardRepository extends JpaRepository<LeaderboardEntity, LeaderboardId> {

    List<LeaderboardEntity> findById_AsOfDateOrderByRankAsc(LocalDate asOfDate);

    @Modifying
    @Query("DELETE FROM LeaderboardEntity e WHERE e.id.asOfDate = :asOfDate")
    void deleteById_AsOfDate(@Param("asOfDate") LocalDate asOfDate);

    @Query(value = "SELECT l.rank AS rank, l.user_id AS userId, u.display_name AS displayName, l.total_points AS totalPoints " +
            "FROM leaderboard l LEFT JOIN users u ON u.user_id = l.user_id WHERE l.as_of_date = :asOfDate ORDER BY l.rank",
            nativeQuery = true)
    List<LeaderboardRowProjection> findRowsByAsOfDate(@Param("asOfDate") LocalDate asOfDate);
}
