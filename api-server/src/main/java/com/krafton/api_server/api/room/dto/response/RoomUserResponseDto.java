package com.krafton.api_server.api.room.dto.response;

import com.krafton.api_server.api.auth.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomUserResponseDto {
    private Long user_id;
    private String username;
    private String nickname;

    @Builder
    public RoomUserResponseDto(Long user_id, String username,String nickname) {
        this.user_id = user_id;
        this.username = username;
        this.nickname = nickname;
    }

    public static RoomUserResponseDto from(User user) {
        if(user == null) return null;

        return RoomUserResponseDto.builder()
                .user_id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }
}
