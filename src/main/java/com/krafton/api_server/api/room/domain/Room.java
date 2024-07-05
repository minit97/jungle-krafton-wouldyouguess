package com.krafton.api_server.api.room.domain;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.game.domain.Game;
import com.krafton.api_server.api.game.domain.GameType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @OneToMany(mappedBy = "room", cascade = ALL, orphanRemoval = true)
    private List<User> userList = new ArrayList<>();

    @OneToOne
    private Game game;

}



