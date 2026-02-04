package com.okayji.feed.entity;

import com.okayji.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "uk_friends_pair",
                columnNames = {"user_low_id", "user_high_id"}
        ),
        indexes = {
                @Index(name = "ix_friends_low", columnList = "user_low_id"),
                @Index(name = "ix_friends_high", columnList = "user_high_id")
        }
)
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_low_id", nullable = false)
    User userLow;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_high_id", nullable = false)
    User userHigh;

    @PrePersist
    @PreUpdate
    void normalizePair() {
        String userLowId = userLow.getId();
        String userHighId = userHigh.getId();
        if (userLowId.equals(userHighId))
            throw new IllegalArgumentException("Cannot friend yourself");
        if (userLowId.compareTo(userHighId) > 0) {
            User tmp = userLow;
            userLow = userHigh;
            userHigh = tmp;
        }
    }
}
