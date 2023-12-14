package com.hawmock.domain.user;

import com.hawmock.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String name;

    private String password;

    private boolean isActivated;

    @Enumerated(EnumType.STRING)
    private SocialProvider socialProvider;

    @Enumerated(EnumType.STRING)
    private Role role;
}
