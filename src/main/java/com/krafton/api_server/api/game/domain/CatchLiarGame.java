package com.krafton.api_server.api.game.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CatchLiarGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catch_liar_game_id")
    private Long id;

    private Integer round;

    @OneToMany(mappedBy = "catchLiarGame")
    @JsonIgnore
    private List<CatchLiarUser> catchLiarUsers = new ArrayList<>();

    @Builder
    public CatchLiarGame(Integer round, CatchLiarUser liar) {
        this.round = round;
    }

    public void addUsers(CatchLiarUser user) {
        user.catchLiarGameStart(this);
        catchLiarUsers.add(user);
    }

}
