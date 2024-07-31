package com.krafton.api_server.api.game1.dto;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.game1.domain.CatchLiarUser;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class CatchLiarResultResponseDto {
    private Long userId;
    private String username;
    private String nickname;
    private Boolean isLiar;
    private String keyword;
    private String imageKey;
    private String imagePath;
    private Boolean isWinner;
    private Integer score;

    public static CatchLiarResultResponseDto from(CatchLiarUser catchLiarUser, User user) {
        if(catchLiarUser == null) return null;

        return CatchLiarResultResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .isLiar(catchLiarUser.getIsLiar())
                .keyword(catchLiarUser.getKeyword())
                .imageKey(catchLiarUser.getImageKey())
                .imagePath(catchLiarUser.getImagePath())
                .isWinner(catchLiarUser.getIsWinner())
                .score(catchLiarUser.getScore())
                .build();
    }

}
