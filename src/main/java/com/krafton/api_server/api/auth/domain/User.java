package com.krafton.api_server.api.auth.domain;

import com.krafton.api_server.api.room.domain.Room;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long kakaoId;
    private String username;
    private String nickname;

    @Builder.Default
    private Long totalPoint = 0L;
    @Setter
    private Long rank;

    private String refreshToken;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public void addPoint(Long point) {
        this.totalPoint += point;
    }

    public void enteredRoom(Room room) {
        this.room = room;
    }

}