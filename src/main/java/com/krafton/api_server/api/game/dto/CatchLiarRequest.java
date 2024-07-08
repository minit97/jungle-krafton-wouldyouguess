package com.krafton.api_server.api.game.dto;

import lombok.Getter;

public class CatchLiarRequest {

    @Getter
    public static class catchLiarKeywordRequest {
        private Long roomId;
        private Long userId;
    }
}
