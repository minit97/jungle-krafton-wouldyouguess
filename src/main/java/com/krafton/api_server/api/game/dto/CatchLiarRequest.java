package com.krafton.api_server.api.game.dto;

import lombok.Getter;

public class CatchLiarRequest {
    @Getter
    public static class CatchLiarStartRequestDto {
        private Long roomId;
    }


    @Getter
    public static class CatchLiarInfoRequestDto {
        private Long catchLiarGameId;
        private Long userId;
        private Integer round;
    }

    @Getter
    public static class CatchLiarVoteRequestDto {
        private Long catchLiarGameId;
        private Long votingUserId;
    }


    @Getter
    public static class CatchLiarResultRequestDto {
        private Long catchLiarGameId;
        private Long userId;
    }
}
