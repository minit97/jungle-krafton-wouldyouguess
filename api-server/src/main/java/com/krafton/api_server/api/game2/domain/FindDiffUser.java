package com.krafton.api_server.api.game2.domain;

import com.krafton.api_server.api.external.vo.AwsS3;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FindDiffUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "find_diff_user_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;
    private String nickname;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "find_diff_game_id")
    private FindDiffGame game;

    private String originalImageKey;
    private String originalImageUrl;
    private String maskingImageKey;
    private String maskingImageUrl;

    private String aiGeneratedImageKey;
    private String aiGeneratedImageUrl;
    private Long maskX1;
    private Long maskY1;
    private Long maskX2;
    private Long maskY2;

    private int score;

    @Builder
    public FindDiffUser(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public void myGameSetting(FindDiffGame game) {
        this.game = game;
    }

    public void updateUploadRequestImage(AwsS3 original, AwsS3 masking) {
        this.originalImageKey = original.getObjectname();
        this.originalImageUrl = original.getPath();
        this.maskingImageKey = masking.getObjectname();
        this.maskingImageUrl = masking.getPath();
    }

    public void uploadAiGeneratedImage(AwsS3 generatedAi, Long x1, Long y1, Long x2, Long y2) {
        this.aiGeneratedImageKey = generatedAi.getObjectname();
        this.aiGeneratedImageUrl = generatedAi.getPath();
        this.maskX1 = x1;
        this.maskY1 = y1;
        this.maskX2 = x2;
        this.maskY2 = y2;
    }

    public void updateScore(int score) {
        this.score += score;
    }
}