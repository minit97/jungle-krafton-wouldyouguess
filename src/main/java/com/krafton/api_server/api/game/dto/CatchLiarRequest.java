package com.krafton.api_server.api.game.dto;

import lombok.Data;

public class CatchLiarRequest {
    @Data
    public static class CatchLiarStartRequestDto {
        private Long roomId;
    }

    @Data
    public static class CatchLiarInfoRequestDto {
        private Long catchLiarGameId;
        private Long userId;
        private Integer round;
    }

    @Data
    public static class CatchLiarVoteRequestDto {
        private Long catchLiarGameId;
        private Long votingUserId;
    }


    @Data
    public static class CatchLiarResultRequestDto {
        private Long catchLiarGameId;
        private Long userId;
    }
}
