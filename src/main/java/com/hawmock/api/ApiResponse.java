package com.hawmock.api;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private ResultCode resultCode;
    private T data;

    @Builder(access = AccessLevel.PROTECTED)
    private ApiResponse(ResultCode resultCode, T data) {
        this.resultCode = resultCode;
        this.data = data;
    }

    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .resultCode(ResultCode.SUCCESS)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .resultCode(ResultCode.SUCCESS)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<List<T>> success(List<T> data) {
        return ApiResponse.<List<T>>builder()
                .resultCode(ResultCode.SUCCESS)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> failure() {
        return ApiResponse.<T>builder()
                .resultCode(ResultCode.FAIL)
                .build();
    }

    @Getter
    public enum ResultCode {
        SUCCESS("성공", 200),
        FAIL("실패", 400);

        private final String message;
        private final int code;

        ResultCode(String message, int code) {
            this.message = message;
            this.code = code;
        }
    }
}
