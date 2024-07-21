package com.krafton.api_server.api.game1.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class CatchLiarRequest {
    @Getter
    @Setter
    public static class CatchLiarStartRequestDto {
        private Long roomId;
    }

    @Getter
    @Setter
    public static class CatchLiarInfoRequestDto {
        private Long catchLiarGameId;
        private Long userId;
        private Integer round;
    }

    @Getter
    @Setter
    public static class CatchLiarVoteRequestDto {
        private Long catchLiarGameId;
        private Long votingUserId;
    }


    @Getter
    @Setter
    public static class CatchLiarResultRequestDto {
        private Long catchLiarGameId;
        private Long userId;
    }
}
