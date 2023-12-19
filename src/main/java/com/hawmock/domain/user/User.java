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

    private boolean isActivated;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private String imageUrl;

    public User(String email, String name, boolean isActivated, SocialType socialType, RoleType roleType, String imageUrl) {
        this.email = email;
        this.name = name;
        this.isActivated = isActivated;
        this.socialType = socialType;
        this.roleType = roleType;
        this.imageUrl = imageUrl;
    }

    public void update(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
