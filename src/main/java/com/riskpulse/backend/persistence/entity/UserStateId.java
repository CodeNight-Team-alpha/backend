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
public class UserStateId implements Serializable {

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "as_of_date")
    private LocalDate asOfDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStateId that = (UserStateId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(asOfDate, that.asOfDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, asOfDate);
    }
}
