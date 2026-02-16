package com.riskpulse.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "source_ref", nullable = false, length = 50)
    private String sourceRef;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    /** Tarih gösterimi için: challenge'ın tamamlandığı gün (as_of_date). */
    @Column(name = "completed_at")
    private LocalDate completedAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
