package com.krafton.api_server.api.game2.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FindDiffGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "find_diff_game_id")
    private Long id;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FindDiffUser> users = new ArrayList<>();

    @Builder
    public FindDiffGame(List<FindDiffUser> users) {
        this.users = users;
    }

    public void addUser(FindDiffUser user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
        user.setGame(this);
    }
}
