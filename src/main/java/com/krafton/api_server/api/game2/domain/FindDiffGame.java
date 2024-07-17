package com.krafton.api_server.api.game2.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class FindDiffGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "find_diff_game_id")
    private Long id;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FindDiffUser> users = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FindDiffImage> images = new ArrayList<>();

    public void addUser(FindDiffUser user) {
        users.add(user);
        user.setGame(this);
    }
}
