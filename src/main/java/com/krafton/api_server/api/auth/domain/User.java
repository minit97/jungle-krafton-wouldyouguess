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

    private String kakaoId;
    private String email;
    private String nickname;
    private Role role;  // 시작 여부
    @Builder.Default
    private Long totalPoint = 0L;
    @Setter
    private Long rank;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public void update(String kakaoId, String email, String nickname) {
        this.kakaoId = kakaoId;
        this.email = email;
        this.nickname = nickname;
    }

    public void addPoint(Long point) {
        this.totalPoint += point;
    }

}