package com.krafton.api_server.api.game1.dto.response;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.game1.domain.CatchLiarUser;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CatchLiarInfoResponseDto {
    private Boolean isLiar;
    private String keyword;
    private Boolean isDrawing;
    private int totalRound;
    private String thisTurnNick;
    private Long thisTurnUserId;
    private String userColor;

    public static CatchLiarInfoResponseDto from(CatchLiarUser requestUser, Integer round, int userCnt, User thisTurnUser) {
        if(requestUser == null || round == null || thisTurnUser == null) return null;

        return CatchLiarInfoResponseDto.builder()
                .isLiar(requestUser.getIsLiar())
                .keyword(requestUser.getKeyword())
                .isDrawing(requestUser.getDrawOrder() == round)
                .totalRound(userCnt)
                .thisTurnNick(thisTurnUser.getUsername())
                .thisTurnUserId(thisTurnUser.getId())
                .userColor(requestUser.getUserColor())
                .build();
    }
}
