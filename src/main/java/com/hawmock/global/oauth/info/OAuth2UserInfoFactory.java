package com.hawmock.global.oauth.info;

import com.hawmock.domain.user.SocialType;
import com.hawmock.global.oauth.info.social.GoogleOAuth2User;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(SocialType socialType, Map<String, Object> attributes) {
        switch (socialType) {
            case GOOGLE: return new GoogleOAuth2User(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
