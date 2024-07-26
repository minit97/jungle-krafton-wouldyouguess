package com.krafton.api_server.api.game1.dto;

import com.krafton.api_server.api.game1.domain.CatchLiarUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatchLiarInfoListResponseDto {
    private Long userId;
    private String userColor;

    public static CatchLiarInfoListResponseDto from(CatchLiarUser catchLiarUser) {
        if(catchLiarUser == null) return null;

        return CatchLiarInfoListResponseDto.builder()
                .userId(catchLiarUser.getUserId())
                .userColor(catchLiarUser.getUserColor())
                .build();
    }

}
