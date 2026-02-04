package com.okayji.feed.entity;

import com.okayji.enums.FriendRequestStatus;
import com.okayji.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name="uk_fr_pair", columnNames={"user_low_id","user_high_id"})
        },
        indexes = {
                @Index(name="ix_fr_low", columnList="user_low_id"),
                @Index(name="ix_fr_high", columnList="user_high_id"),
                @Index(name="ix_fr_requested_by", columnList="requested_by_user_id"),
                @Index(name="ix_fr_status", columnList="status")
        }
)
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_low_id", nullable = false)
    User userLow;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_high_id", nullable = false)
    User userHigh;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="requested_by_user_id", nullable=false)
    User requestedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    FriendRequestStatus status = FriendRequestStatus.PENDING;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    Instant createdAt;

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
