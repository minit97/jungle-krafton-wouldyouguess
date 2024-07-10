package com.krafton.api_server.api.auth.dto;

import com.krafton.api_server.api.auth.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long user_id;
    private String username;
    private String nickname;
    private String accessToken;  // JWT 토큰 등을 반환할 경우
    private String refreshToken;

    @Builder
    public UserResponseDto(Long user_id, String username, String nickname, String accessToken, String refreshToken) {
        this.user_id = user_id;
        this.username = username;
        this.nickname = nickname;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static UserResponseDto from(User user, String accessToken) {
        if(user == null) return null;

        return UserResponseDto.builder()
                .user_id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .accessToken(accessToken)
                .refreshToken(user.getRefreshToken())
                .build();
    }

    public static UserResponseDto from(User user) {
        if(user == null) return null;

        return UserResponseDto.builder()
                .user_id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .refreshToken(user.getRefreshToken())
                .build();
    }
}