package com.krafton.api_server.api.game1.dto;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.game1.domain.CatchLiarUser;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatchLiarInfoResponseDto {
    private Boolean isLiar;
    private String keyword;
    private Boolean isDrawing;
    private int totalRound;
    private String thisTurnNick;
    private Long thisTurnUserId;
    private String userColor;

    public static CatchLiarInfoResponseDto from(CatchLiarUser catchLiarUser, Integer round, int userCnt, User user) {
        if(catchLiarUser == null) return null;

        return CatchLiarInfoResponseDto.builder()
                .isLiar(catchLiarUser.getIsLiar())
                .keyword(catchLiarUser.getKeyword())
                .isDrawing(catchLiarUser.getDrawOrder() == round)
                .totalRound(userCnt)
                .thisTurnNick(user.getUsername())
                .thisTurnUserId(user.getId())
                .userColor(catchLiarUser.getUserColor())
                .build();
    }
}
