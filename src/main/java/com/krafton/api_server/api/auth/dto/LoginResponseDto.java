package com.krafton.api_server.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private boolean success;
    private String message;
    private String accessToken;  // JWT 토큰 등을 반환할 경우
    private String refreshToken;
    private String name;

    // 로그인 실패 시 사용할 생성자
    public LoginResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}