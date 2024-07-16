package com.krafton.api_server.api.game2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class FindDiffResponse {
    @Getter
    @AllArgsConstructor
    public static class FindDiffGeneratedImageResponseDto {
        private String generatedUrl;
        private Long maskX1;
        private Long maskY1;
        private Long maskX2;
        private Long maskY2;
    }


}