package com.krafton.api_server.api.game1.dto.response;

import com.krafton.api_server.api.game1.domain.CatchLiarKeyword;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CatchLiarKeywordResponse {
    private String liarKeyword;
    private String keyword;

    public static CatchLiarKeywordResponse from(CatchLiarKeyword catchLiarKeyword) {
        if(catchLiarKeyword == null) return null;

        return CatchLiarKeywordResponse.builder()
                .liarKeyword(catchLiarKeyword.getLiarKeyword())
                .keyword(catchLiarKeyword.getKeyword())
                .build();
    }
}
