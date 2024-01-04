package com.hawmok.global.oauth.service;

import com.hawmok.domain.user.RoleType;
import com.hawmok.domain.user.SocialType;
import com.hawmok.domain.user.User;
import com.hawmok.domain.user.UserRepository;
import com.hawmok.global.oauth.entity.UserPrincipal;
import com.hawmok.global.oauth.exception.OAuthProviderMissMatchException;
import com.hawmok.global.oauth.info.OAuth2UserInfo;
import com.hawmok.global.oauth.info.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        SocialType socialType = SocialType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialType, user.getAttributes());
        User savedUser = userRepository.findBySocialId(userInfo.getId());

        if (savedUser != null) {
            if (socialType != savedUser.getSocialType()) {
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + socialType +
                                " account. Please use your " + savedUser.getSocialType() + " account to login."
                );
            }
            updateUser(savedUser, userInfo);
        } else {
            savedUser = createUser(userInfo, socialType);
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    private User createUser(OAuth2UserInfo userInfo, SocialType socialType) {
        User user = User.create(
                userInfo.getId(),
                userInfo.getName(),
                userInfo.getEmail(),
                userInfo.getImageUrl(),
                socialType,
                RoleType.USER
        );
        return userRepository.save(user);
    }

    private User updateUser(User user, OAuth2UserInfo userInfo) {
        if (userInfo.getName() != null && !user.getName().equals(userInfo.getName())) {
            user.updateUsername(userInfo.getName());
        }

        if (userInfo.getImageUrl() != null && !user.getProfileImageUrl().equals(userInfo.getImageUrl())) {
            user.updateProfileImageUrl(userInfo.getImageUrl());
        }

        return user;
    }
}
