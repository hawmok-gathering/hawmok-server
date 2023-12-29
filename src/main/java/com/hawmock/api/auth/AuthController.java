package com.hawmock.api.auth;

import com.hawmock.api.ApiResponse;
import com.hawmock.api.auth.request.TokenResponseDto;
import com.hawmock.application.AuthService;
import com.hawmock.domain.user.RoleType;
import com.hawmock.domain.user.UserRefreshToken;
import com.hawmock.domain.user.UserRefreshTokenRepository;
import com.hawmock.global.config.properties.AppProperties;
import com.hawmock.global.error.ErrorCode;
import com.hawmock.global.error.HawmockException;
import com.hawmock.global.oauth.token.AuthToken;
import com.hawmock.global.oauth.token.AuthTokenProvider;
import com.hawmock.global.utils.CookieUtil;
import com.hawmock.global.utils.HeaderUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthTokenProvider tokenProvider;
    private final AuthService authService;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final AppProperties appProperties;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    @GetMapping("/refresh")
    public ApiResponse<TokenResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // access token 확인
        String accessToken = HeaderUtil.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
        if (!authToken.validate()) {
            throw new HawmockException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        // expired access token 인지 확인
        Claims claims = authToken.getExpiredTokenClaims();
        if (claims == null) {
            throw new HawmockException(ErrorCode.NOT_EXPIRED_TOKEN_YET);
        }

        String userId = claims.getSubject();
        RoleType roleType = RoleType.of(claims.get("role", String.class));

        // refresh token
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        if (authRefreshToken.validate()) {
            throw new HawmockException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // userId refresh token 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
        if (userRefreshToken == null) {
            throw new HawmockException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(
                userId,
                roleType.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
        if (validTime <= THREE_DAYS_MSEC) {
            // refresh 토큰 설정
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            authRefreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );

            // DB에 refresh 토큰 업데이트
            userRefreshToken.updateRefreshToken(authRefreshToken.getToken());

            int cookieMaxAge = (int) refreshTokenExpiry / 60;
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
        }

        return ApiResponse.success(newAccessToken.getToken());
    }
}
