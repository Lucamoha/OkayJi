package com.okayji.chat.entity;

import com.okayji.enums.ChatType;
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
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uk_chat_direct_key ", columnNames = "direct_key")
})
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ChatType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    User createdBy;

    String directKey;

    String chatAvatarUrl;

    String chatName;

    @CreationTimestamp
    @Column(nullable=false, updatable=false)
    Instant createdAt;

    @Builder.Default
    Instant lastMessageAt = Instant.now();

    @Builder.Default
    Long lastMessageSeq = 0L;
}
