package com.krafton.api_server.api.game2.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FindDiffUser {

    @Id @GeneratedValue
    @Column(name = "find_diff_user_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "find_diff_game_id")
    @Setter
    private FindDiffGame game;

    @OneToOne(mappedBy = "user")
    private FindDiffImage image;

    private Long score;

    @Builder
    public FindDiffUser(Long userId) {
        this.userId = userId;
        this.score = 0L;
    }

    public void addScore(Long gameScore) {
        this.score += gameScore;
    }

    public void updateImage(FindDiffImage image) {
        this.image = image;
    }
}