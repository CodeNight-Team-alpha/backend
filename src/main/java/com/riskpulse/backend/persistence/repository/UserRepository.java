package com.riskpulse.backend.persistence.repository;

import com.riskpulse.backend.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    /** İsim ile giriş için; ilk eşleşen kullanıcı (büyük/küçük harf duyarsız). */
    Optional<UserEntity> findFirstByDisplayNameIgnoreCase(String displayName);
}
