package com.krafton.api_server.api.room.dto;

import lombok.Getter;

public class RoomRequest {

    @Getter
    public static class RoomCreateRequest {
        private Long userId;
        private Long roomId;
    }
}
