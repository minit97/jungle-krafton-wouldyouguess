package com.krafton.api_server.api.room.dto;

import com.krafton.api_server.api.auth.domain.User;
import lombok.Builder;
import lombok.Getter;

public class RoomRequest {

    @Getter
    public static class RoomCreateRequest {
        private Long userId;
    }

    @Getter
    public static class RoomUser {
        private Long user_id;
        private String nickname;

        @Builder
        public RoomUser(Long user_id, String nickname) {
            this.user_id = user_id;
            this.nickname = nickname;
        }

        public static RoomUser from(User user) {
            if(user == null) return null;

            return RoomUser.builder()
                    .user_id(user.getId())
                    .nickname(user.getNickname())
                    .build();
        }
    }
}
