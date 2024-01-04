package com.hawmok.application;

import com.hawmok.api.request.RefreshTokenRequest;
import com.hawmok.domain.user.RoleType;
import com.hawmok.domain.user.UserRefreshToken;
import com.hawmok.domain.user.UserRefreshTokenRepository;
import com.hawmok.global.config.properties.AppProperties;
import com.hawmok.global.error.ErrorCode;
import com.hawmok.global.error.HawmockException;
import com.hawmok.global.oauth.token.AuthToken;
import com.hawmok.global.oauth.token.AuthTokenProvider;
import com.hawmok.global.utils.CookieUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private static final long THREE_DAYS_MSEC = 259200000;
    private static final String REFRESH_TOKEN = "refresh_token";

    public String refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {
        String accessToken = refreshTokenRequest.accessToken();
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);

        validateAccessToken(authToken);

        Claims claims = authToken.getExpiredTokenClaims();

        String userId = claims.getSubject();
        RoleType roleType = RoleType.of(claims.get("role", String.class));

        String refreshToken = refreshTokenRequest.refreshToken();
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        validateRefreshToken(authRefreshToken);

        UserRefreshToken userRefreshToken = getUserRefreshToken(userId, refreshToken);

        Date now = new Date();
        AuthToken newAccessToken = createNewAccessToken(userId, roleType, now);

        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        if (validTime <= THREE_DAYS_MSEC) {
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            authRefreshToken = createNewAuthTokenWithExpiry(refreshTokenExpiry, now);

            updateRefreshTokenInDatabase(userRefreshToken, authRefreshToken);

            updateRefreshTokenCookie(refreshTokenRequest, authRefreshToken);
        }

        return newAccessToken.getToken();
    }

    private void validateAccessToken(AuthToken authToken) {
        if (!authToken.validate()) {
            throw new HawmockException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    private void validateRefreshToken(AuthToken authRefreshToken) {
        if (authRefreshToken.validate()) {
            throw new HawmockException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    private UserRefreshToken getUserRefreshToken(String userId, String refreshToken) {
        return userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken)
                .orElseThrow(() -> new HawmockException(ErrorCode.INVALID_REFRESH_TOKEN));
    }

    private AuthToken createNewAccessToken(String userId, RoleType roleType, Date now) {
        return tokenProvider.createAuthToken(
                userId,
                roleType.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );
    }

    private AuthToken createNewAuthTokenWithExpiry(long expiry, Date now) {
        return tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + expiry)
        );
    }

    private void updateRefreshTokenInDatabase(UserRefreshToken userRefreshToken, AuthToken authRefreshToken) {
        userRefreshToken.updateRefreshToken(authRefreshToken.getToken());
    }

    private void updateRefreshTokenCookie(RefreshTokenRequest refreshTokenRequest, AuthToken authRefreshToken) {
        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        int cookieMaxAge = (int) refreshTokenExpiry / 60;

        CookieUtil.deleteCookie(refreshTokenRequest.request(), refreshTokenRequest.response(), REFRESH_TOKEN);
        CookieUtil.addCookie(
                refreshTokenRequest.response(),
                REFRESH_TOKEN,
                authRefreshToken.getToken(),
                cookieMaxAge
        );
    }
}

