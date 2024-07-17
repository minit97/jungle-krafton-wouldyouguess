package com.krafton.api_server.api.game2.dto;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.auth.dto.UserResponseDto;
import com.krafton.api_server.api.game2.domain.FindDiffUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class FindDiffResponse {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class FindDiffGeneratedImageResponseDto {
        private String generatedUrl;
        private Long maskX1;
        private Long maskY1;
        private Long maskX2;
        private Long maskY2;

        public static FindDiffGeneratedImageResponseDto from(FindDiffUser user) {
            if(user == null) return null;

            return FindDiffGeneratedImageResponseDto.builder()
                    .generatedUrl(user.getGeneratedImageUrl())
                    .maskX1(user.getMaskX1())
                    .maskX2(user.getMaskX2())
                    .maskY1(user.getMaskY1())
                    .maskY2(user.getMaskY2())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class FindDiffResultDto {
        private String maskingImageKey;
        private String maskingImageUrl;
        private String generatedImageKey;
        private String generatedImageUrl;

        public static FindDiffResultDto from(FindDiffUser user) {
            if(user == null) return null;

            return FindDiffResultDto.builder()
                    .maskingImageKey(user.getMaskingImageKey())
                    .maskingImageUrl(user.getMaskingImageUrl())
                    .generatedImageKey(user.getGeneratedImageKey())
                    .generatedImageUrl(user.getGeneratedImageUrl())
                    .build();
        }
    }


}