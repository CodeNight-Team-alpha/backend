package com.riskpulse.backend.persistence.repository;

import com.riskpulse.backend.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
