package com.hawmok.domain.user;

import com.hawmok.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String socialId;

    private String email;

    private String name;

    private boolean isActivated;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private String profileImageUrl;


    private User (String socialId, String name, String email, String imageUrl, SocialType socialType, RoleType roleType) {
        this.socialId = socialId;
        this.name = name;
        this.email = email;
        this.profileImageUrl = imageUrl;
        this.socialType = socialType;
        this.roleType = roleType;
    }


    public static User create(String socialId, String name, String email, String imageUrl, SocialType socialType, RoleType roleType) {
        return new User(socialId, name, email, imageUrl, socialType, roleType);
    }

    public void updateUsername(String name) {
        this.name = name;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
