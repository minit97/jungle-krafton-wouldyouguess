package com.krafton.api_server.api.game1.dto;

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

    public static CatchLiarInfoResponseDto from(CatchLiarUser catchLiarUser, Integer round, int userCnt, String thisTurnNick) {
        if(catchLiarUser == null) return null;

        return CatchLiarInfoResponseDto.builder()
                .isLiar(catchLiarUser.getIsLiar())
                .keyword(catchLiarUser.getKeyword())
                .isDrawing(catchLiarUser.getDrawOrder() == round)
                .totalRound(userCnt)
                .thisTurnNick(thisTurnNick)
                .build();
    }
}
