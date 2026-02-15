package com.okayji.feed.entity;

import com.okayji.enums.PostMediaType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(indexes = {@Index(name = "idx_post", columnList = "post_id")})
public class PostMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    Post post;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PostMediaType type;

    @Column(nullable = false)
    String mediaUrl;
}
