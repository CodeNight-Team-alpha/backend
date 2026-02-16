package com.riskpulse.backend.persistence.repository;

import com.riskpulse.backend.persistence.entity.ChallengeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<ChallengeEntity, String> {

    List<ChallengeEntity> findByIsActiveTrueOrderByPriorityAsc();
}
