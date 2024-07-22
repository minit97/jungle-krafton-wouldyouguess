package com.krafton.api_server.api.game2.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @NoArgsConstructor
    public static class FindDiffImageUploadRequestDto {
        private Long userId;
        private Long gameId;
        private MultipartFile originalImage;
        private MultipartFile maskingImage;
        private Long maskX1;
        private Long maskY1;
        private Long maskX2;
        private Long maskY2;

        @Builder
        public FindDiffImageUploadRequestDto(Long userId, Long gameId, MultipartFile originalImage, MultipartFile maskingImage, Long maskX1, Long maskY1, Long maskX2, Long maskY2) {
            this.userId = userId;
            this.gameId = gameId;
            this.originalImage = originalImage;
            this.maskingImage = maskingImage;
            this.maskX1 = maskX1;
            this.maskY1 = maskY1;
            this.maskX2 = maskX2;
            this.maskY2 = maskY2;
        }
    }

    @Getter
    @Setter
    public static class FindDiffScoreRequestDto {
        private Long userId;
        private int chance;
        private Boolean isFound;
    }

}
