package com.hawmok.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    FAILED_MESSAGE("서버에서 오류가 발생하였습니다."),
    INVALID_ACCESS_TOKEN("Invalid access token."),
    INVALID_REFRESH_TOKEN("Invalid refresh token."),
    NOT_EXPIRED_TOKEN_YET("Not expired token yet.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
