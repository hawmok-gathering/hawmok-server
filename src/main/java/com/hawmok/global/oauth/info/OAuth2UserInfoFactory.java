package com.hawmok.global.oauth.info;

import com.hawmok.domain.user.SocialType;
import com.hawmok.global.oauth.info.impl.GoogleOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(SocialType socialType, Map<String, Object> attributes) {
        return switch (socialType) {
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
        };
    }
}
