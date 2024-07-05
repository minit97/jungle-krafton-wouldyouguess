package com.krafton.api_server.api.game.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    private GameType gameType;

    @Builder
    public Game(GameType gameType) {
        this.gameType = gameType;
    }
}
