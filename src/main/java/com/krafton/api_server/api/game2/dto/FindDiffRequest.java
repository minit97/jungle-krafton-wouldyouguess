package com.krafton.api_server.api.game2.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class FindDiffRequest {
    @Getter
    @Setter
    public static class FindDiffRequestDto {
        private Long roomId;
    }

    @Getter
    @Setter
    public static class FindDiffUserRequestDto {
        private Long userId;
    }

    @Getter
    @Setter
    public static class FindDiffImageRequestDto {
        private Long userId;
        private Long roomId;
        private MultipartFile image;
    }

    @Getter
    @Setter
    public static class FindDiffGeneratedImageRequestDto {
        private Long userId;
        private Long roomId;
        private MultipartFile image;
        private MultipartFile mask;
        private Long maskX1;
        private Long maskY1;
        private Long maskX2;
        private Long maskY2;

    }

    @Getter
    @Setter
    public static class FindDiffScoreRequestDto {
        private Long gameId;
        private Long userId;
        private Long score;
    }


}
