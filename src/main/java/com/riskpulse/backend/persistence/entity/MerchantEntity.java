package com.riskpulse.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "merchants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantEntity {

    @Id
    @Column(name = "merchant_id", length = 50)
    private String merchantId;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
