package com.krafton.api_server.api.game1.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class CatchLiarRequest {
    @Getter
    @Setter
    @Builder
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
    @Builder
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

    @Getter
    @Setter
    public static class CatchLiarImageUploadRequestDto {
        private Long userId;
        private Long catchLiarGameId;
        private MultipartFile file;
    }

    @Setter
    @Getter
    public static class CatchLiarRemoveRequestDto {
        private String imageKey;
    }

}
