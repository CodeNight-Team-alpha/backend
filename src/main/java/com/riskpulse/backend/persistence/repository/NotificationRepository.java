package com.riskpulse.backend.persistence.repository;

import com.riskpulse.backend.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    boolean existsByUserIdAndSourceRef(String userId, String sourceRef);
}
