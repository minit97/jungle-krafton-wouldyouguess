package com.krafton.api_server.api.auth.domain;

import com.krafton.api_server.api.room.domain.Room;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private Long kakaoId;
    private String username;
    private String nickname;
    private String refreshToken;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    private Long totalPoint;
    private Long rank;


    @Builder
    public User(Long kakaoId, String username, String refreshToken) {
        this.kakaoId = kakaoId;
        this.username = username;
        this.refreshToken = refreshToken;
        this.totalPoint = 0L;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public void addPoint(Long point) {
        this.totalPoint += point;
    }

    public void enteredRoom(Room room) {
        this.room = room;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}