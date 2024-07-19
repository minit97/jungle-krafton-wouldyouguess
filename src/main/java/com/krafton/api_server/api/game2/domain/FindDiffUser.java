package com.krafton.api_server.api.game2.domain;

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

    private String generatedImageKey;
    private String generatedImageUrl;
    private Long maskX1;
    private Long maskY1;
    private Long maskX2;
    private Long maskY2;

    private Long score;

    @Builder
    public FindDiffUser(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public void updateScore(Long score) {
        this.score += score;
    }

    public void uploadOriginalImage(String origianlKey, String originalImageUrl, String maskingImageKey, String maskingImageUrl) {
        this.originalImageKey = origianlKey;
        this.originalImageUrl = originalImageUrl;
        this.maskingImageKey = maskingImageKey;
        this.maskingImageUrl = maskingImageUrl;
    }

    public void uploadAiGeneratedImage(String key, String imageUrl, Long x1, Long y1, Long x2, Long y2) {
        this.generatedImageKey = key;
        this.generatedImageUrl = imageUrl;
        this.maskX1 = x1;
        this.maskY1 = y1;
        this.maskX2 = x2;
        this.maskY2 = y2;
    }

    @Override
    public String toString() {
        return "FindDiffUser{" +
                "id=" + id +
                ", userId=" + userId +
                ", originalImageKey='" + originalImageKey + '\'' +
                ", originalImageUrl='" + originalImageUrl + '\'' +
                ", maskingImageKey='" + maskingImageKey + '\'' +
                ", maskingImageUrl='" + maskingImageUrl + '\'' +
                ", generatedImageKey='" + generatedImageKey + '\'' +
                ", generatedImageUrl='" + generatedImageUrl + '\'' +
                ", maskX1=" + maskX1 +
                ", maskY1=" + maskY1 +
                ", maskX2=" + maskX2 +
                ", maskY2=" + maskY2 +
                ", score=" + score +
                '}';
    }

    public void setGame(FindDiffGame game) {
        this.game = game;
    }
}