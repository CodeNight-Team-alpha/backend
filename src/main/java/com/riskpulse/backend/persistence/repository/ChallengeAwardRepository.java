package com.riskpulse.backend.persistence.repository;

import com.riskpulse.backend.persistence.entity.ChallengeAwardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChallengeAwardRepository extends JpaRepository<ChallengeAwardEntity, String> {

    Optional<ChallengeAwardEntity> findByUserIdAndAsOfDate(String userId, LocalDate asOfDate);

    List<ChallengeAwardEntity> findByAsOfDate(LocalDate asOfDate);
}
