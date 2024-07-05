package com.krafton.api_server.api.room.dto;

import com.krafton.api_server.api.game.domain.GameType;
import lombok.Getter;

import java.util.List;

public class RoomRequest {

    @Getter
    public static class RoomCreateRequest {
        private Long userId;
    }
}
