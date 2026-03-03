package com.okayji.notification.entity;

import com.okayji.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        indexes = {
                @Index(name = "idx_notification_user_created", columnList = "user_id,created_at DESC"),
                @Index(name = "idx_notification_user_unread", columnList = "user_id,is_read")
        }
)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    String payload;

    @Enumerated(EnumType.STRING)
    NotificationType type;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    boolean read = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    Instant createdAt;
}
