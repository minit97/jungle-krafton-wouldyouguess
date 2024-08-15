package com.krafton.api_server.api.room.domain;

import com.krafton.api_server.api.auth.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> participants = new ArrayList<>();

    @Builder
    public Room(User user) {
        joinRoom(user);
    }

    public void joinRoom(User user) {
        user.enteredRoom(this);
        this.participants.add(user);
    }

    public void exitRoom(User user) {
        user.enteredRoom(null);
        this.participants.remove(user);
    }
}



