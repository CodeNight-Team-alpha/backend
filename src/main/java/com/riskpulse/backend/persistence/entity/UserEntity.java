package com.riskpulse.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "display_name", length = 255)
    private String displayName;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
