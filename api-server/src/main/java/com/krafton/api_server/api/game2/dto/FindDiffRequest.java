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
    public static class FindDiffImageUploadRequestDto {
        private Long userId;
        private Long gameId;
        private MultipartFile originalImage;
        private MultipartFile maskingImage;
        private Long maskX1;
        private Long maskY1;
        private Long maskX2;
        private Long maskY2;
    }

    @Getter
    @Setter
    public static class FindDiffScoreRequestDto {
        private Long userId;
        private Long gameId;
        private int chance;
        private Boolean isFound;
    }

}
