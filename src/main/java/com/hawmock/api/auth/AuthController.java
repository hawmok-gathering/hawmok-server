package com.hawmock.api.auth;

import com.hawmock.api.ApiResponse;
import com.hawmock.api.request.RefreshTokenRequest;
import com.hawmock.application.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/refresh")
    public ApiResponse<String> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.of(request, response);
        String newAccessToken = authService.refreshAccessToken(refreshTokenRequest);
        return ApiResponse.success(newAccessToken);
    }
}
