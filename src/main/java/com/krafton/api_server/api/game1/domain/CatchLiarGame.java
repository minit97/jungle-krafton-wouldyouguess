package com.krafton.api_server.api.game1.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "catch_liar_keyword_id")
    private CatchLiarKeyword catchLiarKeyword;

    @Builder
    public CatchLiarGame(Integer round, CatchLiarUser liar, CatchLiarKeyword catchLiarKeyword) {
        this.round = round;
        this.catchLiarKeyword = catchLiarKeyword;
    }

    public void addUsers(CatchLiarUser user) {
        user.catchLiarGameStart(this);
        catchLiarUsers.add(user);
    }

}
