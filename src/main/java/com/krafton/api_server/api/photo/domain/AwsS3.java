package com.krafton.api_server.api.photo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AwsS3 {
    private String key;         // 객체이름
    private String path;        // 해당 객체의 절대 경로

    @Builder
    public AwsS3(String key, String path) {
        this.key = key;
        this.path = path;
    }
}