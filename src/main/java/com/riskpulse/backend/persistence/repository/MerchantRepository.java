package com.riskpulse.backend.persistence.repository;

import com.riskpulse.backend.persistence.entity.MerchantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<MerchantEntity, String> {
}
