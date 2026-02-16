package com.riskpulse.backend.persistence.repository;

import com.riskpulse.backend.persistence.entity.UserStateEntity;
import com.riskpulse.backend.persistence.entity.UserStateId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserStateRepository extends JpaRepository<UserStateEntity, UserStateId> {

    List<UserStateEntity> findById_AsOfDate(LocalDate asOfDate);
}
