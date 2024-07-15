package com.krafton.api_server.api.game.dto;

import com.krafton.api_server.api.game.domain.CatchLiarUser;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatchLiarVoteCandidatesResponseDto {
    private Long userId;
    private String imageKey;
    private String imagePath;


    public static CatchLiarVoteCandidatesResponseDto from(CatchLiarUser catchLiarUser) {
        if(catchLiarUser == null) return null;

        return CatchLiarVoteCandidatesResponseDto.builder()
                .userId(catchLiarUser.getUserId())
                .imageKey(catchLiarUser.getImageKey())
                .imagePath(catchLiarUser.getImagePath())
                .build();
    }
}
