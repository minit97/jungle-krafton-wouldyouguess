package com.krafton.api_server.api.game.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Getter
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "catch_liar_game_id")
    private CatchLiarGame catchLiarGame;

    @Builder
    public CatchLiarUser(Long userId, Boolean isLiar, String keyword, Integer drawOrder) {
        this.userId = userId;
        this.isLiar = isLiar;
        this.keyword = keyword;
        this.drawOrder = drawOrder;
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
}