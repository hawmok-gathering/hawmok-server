package com.hawmok.global.error;

import lombok.Getter;

@Getter
public class HawmockException extends RuntimeException {
    private ErrorCode errorCode;

    public HawmockException(String message) {
        super(message);
    }

    public HawmockException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
