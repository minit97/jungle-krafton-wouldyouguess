package com.krafton.api_server.api.auth.domain;

import com.krafton.api_server.api.room.domain.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

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

    private String username;
    private String email;
    private Role role;  // 시작 여부


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

}