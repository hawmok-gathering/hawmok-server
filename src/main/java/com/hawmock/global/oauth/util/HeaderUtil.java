package com.hawmock.global.oauth.util;

import jakarta.servlet.http.HttpServletRequest;

public class HeaderUtil {
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    public static String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(HEADER_AUTHORIZATION);

        if (accessToken != null && accessToken.startsWith(TOKEN_PREFIX)) {
            return accessToken.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
