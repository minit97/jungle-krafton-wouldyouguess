package com.krafton.api_server.api.game1.dto.response;

import com.krafton.api_server.api.game1.domain.CatchLiarUser;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CatchLiarImageResponseDto {
    private String imageKey;
    private String imagePath;

    public static CatchLiarImageResponseDto from(CatchLiarUser catchLiarUser) {
        if(catchLiarUser == null) return null;

        return CatchLiarImageResponseDto.builder()
                .imageKey(catchLiarUser.getImageKey())
                .imagePath(catchLiarUser.getImagePath())
                .build();
    }
}
