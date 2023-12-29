package com.hawmock.api.request;

import com.hawmock.global.oauth.util.HeaderUtil;
import com.hawmock.global.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.hawmock.global.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN;

public record RefreshTokenRequest(
        String accessToken,
        String refreshToken,
        HttpServletRequest request,
        HttpServletResponse response
) {

    public static RefreshTokenRequest of(HttpServletRequest request, HttpServletResponse response) {
        return new RefreshTokenRequest(
                HeaderUtil.getAccessToken(request),
                CookieUtil.getCookie(request, REFRESH_TOKEN)
                        .map(Cookie::getValue)
                        .orElse(null),
                request,
                response
        );
    }
}
