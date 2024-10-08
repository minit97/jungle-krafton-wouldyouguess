package com.krafton.api_server.api.game1.domain;

import com.krafton.api_server.api.external.vo.AwsS3;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CatchLiarUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catch_liar_user_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private Boolean isLiar;
    private String keyword;
    private Integer drawOrder;
    private Integer votedCount;

    private String imageKey;
    private String imagePath;

    private Boolean isWinner;
    private Integer score;
    private String userColor;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "catch_liar_game_id")
    private CatchLiarGame catchLiarGame;

    @Version
    private Long version;

    @Builder
    public CatchLiarUser(Long userId, Boolean isLiar, String keyword, Integer drawOrder, String userColor) {
        this.userId = userId;
        this.isLiar = isLiar;
        this.keyword = keyword;
        this.drawOrder = drawOrder;
        this.votedCount = 0;
        this.userColor = userColor;
    }

    public void catchLiarGameStart(CatchLiarGame catchLiarGame) {
        this.catchLiarGame = catchLiarGame;
    }

    public void updateVotedCount() {
        this.votedCount += 1;
    }

    public Boolean isLiar() {
        return isLiar;
    }

    public void updateUploadImageS3(AwsS3 image) {
        this.imageKey = image.getObjectname();
        this.imagePath = image.getPath();
    }

    public void updateResult (Boolean result) {
        this.isWinner = result;
        this.score = result ? 10 : 0;
    }
}