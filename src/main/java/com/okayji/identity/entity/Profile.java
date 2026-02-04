package com.okayji.identity.entity;

import com.okayji.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Profile {
    @Id
    @Column(name = "user_id")
    @EqualsAndHashCode.Include
    String userId;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    User user;
    String fullName;
    @Enumerated(EnumType.STRING)
    Gender gender = Gender.OTHER;
    String bio;
    LocalDate birthday;
    String avatarUrl;
    String coverImageUrl;
}
