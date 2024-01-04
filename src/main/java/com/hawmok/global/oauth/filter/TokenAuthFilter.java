package com.hawmok.global.oauth.filter;

import com.hawmok.global.oauth.token.AuthToken;
import com.hawmok.global.oauth.token.AuthTokenProvider;
import com.hawmok.global.oauth.util.HeaderUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {

    private final AuthTokenProvider authTokenProvider;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String accessToken = HeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(accessToken);

        if (authToken.validate()) {
            Authentication authentication = authTokenProvider.getAuthentication(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
