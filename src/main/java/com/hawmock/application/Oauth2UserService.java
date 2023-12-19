package com.hawmock.application;

import com.hawmock.domain.user.RoleType;
import com.hawmock.domain.user.SocialType;
import com.hawmock.domain.user.User;
import com.hawmock.domain.user.UserRepository;
import com.hawmock.global.oauth.exception.OAuthProviderMissMatchException;
import com.hawmock.global.oauth.info.OAuth2UserInfo;
import com.hawmock.global.oauth.info.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        SocialType socialType = SocialType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialType, user.getAttributes());

        User savedUser = userRepository.findUserByEmailAndSocialType(userInfo.getEmail(), socialType);

        if (savedUser != null) {
            if (socialType != savedUser.getSocialType()) {
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + socialType + " account. Please use your " + savedUser.getSocialType() + " account to login."
                );
            }
            updateUser(savedUser, userInfo);
        } else {
            savedUser = createUser(userInfo, socialType);
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    private User createUser(OAuth2UserInfo userInfo, SocialType socialType) {
        User user = new User(
                userInfo.getEmail(),
                userInfo.getName(),
                true,
                socialType,
                RoleType.USER,
                userInfo.getImageUrl()
        );

        return userRepository.save(user);
    }

    private void updateUser(User user, OAuth2UserInfo userInfo) {
        if (isUpdatedUsername(user, userInfo) || isUpdatedUserImageUrl(user, userInfo)) {
            user.update(userInfo.getName(), userInfo.getImageUrl());
        }
    }

    private static boolean isUpdatedUserImageUrl(User user, OAuth2UserInfo userInfo) {
        return userInfo.getImageUrl() != null && !user.getImageUrl().equals(userInfo.getImageUrl());
    }

    private static boolean isUpdatedUsername(User user, OAuth2UserInfo userInfo) {
        return userInfo.getName() != null && !user.getName().equals(userInfo.getName());
    }
}
