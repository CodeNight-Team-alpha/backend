package com.riskpulse.backend.persistence.repository;

import com.riskpulse.backend.persistence.entity.BadgeAwardEntity;
import com.riskpulse.backend.persistence.entity.BadgeAwardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BadgeAwardRepository extends JpaRepository<BadgeAwardEntity, BadgeAwardId> {

    @Query("SELECT COUNT(e) > 0 FROM BadgeAwardEntity e WHERE e.id.userId = :userId AND e.id.badgeId = :badgeId")
    boolean existsByUserIdAndBadgeId(@Param("userId") String userId, @Param("badgeId") String badgeId);

    @Query("SELECT e FROM BadgeAwardEntity e WHERE e.id.userId = :userId")
    List<BadgeAwardEntity> findById_UserId(@Param("userId") String userId);

    @Query("SELECT e FROM BadgeAwardEntity e WHERE e.id.userId IN :userIds")
    List<BadgeAwardEntity> findById_UserIdIn(@Param("userIds") List<String> userIds);
}
