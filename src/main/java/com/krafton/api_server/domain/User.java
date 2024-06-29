package com.krafton.api_server.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String username;

    @Enumerated(STRING)
    private Game game;

    @OneToMany(mappedBy = "user")
    private List<Photo> photos;

    private int score;

    @Enumerated(STRING)
    private Role role;
}
