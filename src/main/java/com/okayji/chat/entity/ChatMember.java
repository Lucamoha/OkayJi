package com.okayji.chat.entity;

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
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        uniqueConstraints = {@UniqueConstraint(name = "uk_chat_member",
                columnNames = {"chat_id", "member_id"})},
        indexes = {@Index(name = "idx_chat_id", columnList = "chat_id"),
                @Index(name = "idx_member_id", columnList = "member_id")}
)
public class ChatMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    Chat chat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    User member;

    @Builder.Default
    Long lastReadSeq = 0L;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    Instant joinedAt;
}
