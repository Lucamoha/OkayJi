package com.okayji.chat.entity;

import com.okayji.enums.MessageType;
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
        uniqueConstraints = @UniqueConstraint(
                name="uk_msg_chat_seq", columnNames={"chat_id","seq"}),
        indexes = {@Index(name = "idx_msg_chat_seq", columnList = "chat_id,seq")}
)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    Chat chat;

    @Column(nullable = false)
    Long seq; // tăng dần trong 1 chat

    @Enumerated(EnumType.STRING)
    MessageType type;

    @Lob
    String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    User sender;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    Instant createdAt;
}
