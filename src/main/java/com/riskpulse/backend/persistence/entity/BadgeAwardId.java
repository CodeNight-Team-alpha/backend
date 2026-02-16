package com.riskpulse.backend.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeAwardId implements Serializable {

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "badge_id", length = 50)
    private String badgeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadgeAwardId that = (BadgeAwardId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(badgeId, that.badgeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, badgeId);
    }
}
