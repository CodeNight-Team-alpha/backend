package com.riskpulse.backend.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderboardId implements Serializable {

    @Column(name = "as_of_date")
    private LocalDate asOfDate;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaderboardId that = (LeaderboardId) o;
        return Objects.equals(asOfDate, that.asOfDate) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(asOfDate, userId);
    }
}
