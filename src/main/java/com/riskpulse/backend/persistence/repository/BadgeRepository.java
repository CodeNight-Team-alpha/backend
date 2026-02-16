package com.riskpulse.backend.persistence.repository;

import com.riskpulse.backend.persistence.entity.BadgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<BadgeEntity, String> {

    List<BadgeEntity> findAllByOrderByThresholdPointsAsc();
}
