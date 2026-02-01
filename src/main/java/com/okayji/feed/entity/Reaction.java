package com.okayji.feed.entity;

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
        uniqueConstraints = {@UniqueConstraint(name = "uk_user_post",
                columnNames = {"post_id", "user_id"})},
        indexes = {@Index(name = "idx_post", columnList = "post_id")}
)
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    Instant createdAt;
}
