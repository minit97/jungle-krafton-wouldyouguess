package com.krafton.api_server.api.external.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AwsS3 {
    private String objectname;
    private String path;

    @Builder
    public AwsS3(String objectname, String path) {
        this.objectname = objectname;
        this.path = path;
    }
}