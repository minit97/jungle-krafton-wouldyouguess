package com.krafton.api_server.api.game2.dto;

import com.krafton.api_server.api.game2.domain.FindDiffUser;
import lombok.Builder;
import lombok.Getter;

public class FindDiffResponse {
    @Getter
    @Builder
    public static class FindDiffAiGeneratedImageResponseDto {
        private String aiGeneratedImageUrl;
        private Long maskX1;
        private Long maskY1;
        private Long maskX2;
        private Long maskY2;

        public static FindDiffAiGeneratedImageResponseDto from(FindDiffUser user) {
            if(user == null) return null;

            return FindDiffAiGeneratedImageResponseDto.builder()
                    .aiGeneratedImageUrl(user.getAiGeneratedImageUrl())
                    .maskX1(user.getMaskX1())
                    .maskX2(user.getMaskX2())
                    .maskY1(user.getMaskY1())
                    .maskY2(user.getMaskY2())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class FindDiffGameImagesDto {
        private String originalImageKey;
        private String originalImageUrl;
        private String maskingImageKey;
        private String maskingImageUrl;

        private String aiGeneratedImageKey;
        private String aiGeneratedImageUrl;
        private Long maskX1;
        private Long maskY1;
        private Long maskX2;
        private Long maskY2;

        public static FindDiffGameImagesDto from(FindDiffUser user) {
            if(user == null) return null;

            return FindDiffGameImagesDto.builder()
                    .originalImageKey(user.getOriginalImageKey())
                    .originalImageUrl(user.getOriginalImageUrl())
                    .maskingImageKey(user.getMaskingImageKey())
                    .maskingImageUrl(user.getMaskingImageUrl())
                    .aiGeneratedImageKey(user.getAiGeneratedImageKey())
                    .aiGeneratedImageUrl(user.getAiGeneratedImageUrl())
                    .maskX1(user.getMaskX1())
                    .maskX2(user.getMaskX2())
                    .maskY1(user.getMaskY1())
                    .maskY2(user.getMaskY2())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class FindDiffResultDto {
        private String nickname;
        private Integer score;

        public static FindDiffResultDto from(FindDiffUser user) {
            if (user == null) return null;

            return FindDiffResultDto.builder()
                    .nickname(user.getNickname())
                    .score(user.getScore())
                    .build();
        }
    }

}