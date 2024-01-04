package com.hawmok.api.request;

import com.hawmok.global.oauth.util.HeaderUtil;
import com.hawmok.global.utils.CookieUtil;
import com.hawmok.global.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public record RefreshTokenRequest(
        String accessToken,
        String refreshToken,
        HttpServletRequest request,
        HttpServletResponse response
) {

    public static RefreshTokenRequest of(HttpServletRequest request, HttpServletResponse response) {
        return new RefreshTokenRequest(
                HeaderUtil.getAccessToken(request),
                CookieUtil.getCookie(request, OAuth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN)
                        .map(Cookie::getValue)
                        .orElse(null),
                request,
                response
        );
    }
}
