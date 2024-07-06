package com.krafton.api_server.api.room.domain;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.game.domain.Game;
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
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @OneToOne
    private User creater;

    @OneToMany(mappedBy = "room")
    private List<User> participants = new ArrayList<>();

    @OneToOne
    private Game game;

    @Builder
    public Room(User creater) {
        this.creater = creater;
    }

    public void joinRoom(User user) {
        this.participants.add(user);
    }

    public void exitRoom(User user) {
        this.participants.remove(user);
    }
}



