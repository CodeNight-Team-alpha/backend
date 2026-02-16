package com.riskpulse.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_state")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStateEntity {

    @EmbeddedId
    private UserStateId id;

    @Column(name = "spend_today", nullable = false, precision = 12, scale = 2)
    private BigDecimal spendToday;

    @Column(name = "unique_categories_today", nullable = false)
    private Integer uniqueCategoriesToday;

    @Column(name = "electronics_spend_today", nullable = false, precision = 12, scale = 2)
    private BigDecimal electronicsSpendToday;

    @Column(name = "spend_7d", nullable = false, precision = 12, scale = 2)
    private BigDecimal spend7d;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public static UserStateEntity of(String userId, LocalDate asOfDate, BigDecimal spendToday,
                                    Integer uniqueCategoriesToday, BigDecimal electronicsSpendToday,
                                    BigDecimal spend7d) {
        return UserStateEntity.builder()
                .id(UserStateId.builder().userId(userId).asOfDate(asOfDate).build())
                .spendToday(spendToday != null ? spendToday : BigDecimal.ZERO)
                .uniqueCategoriesToday(uniqueCategoriesToday != null ? uniqueCategoriesToday : 0)
                .electronicsSpendToday(electronicsSpendToday != null ? electronicsSpendToday : BigDecimal.ZERO)
                .spend7d(spend7d != null ? spend7d : BigDecimal.ZERO)
                .createdAt(OffsetDateTime.now())
                .build();
    }
}
