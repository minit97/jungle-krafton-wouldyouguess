package com.krafton.api_server.api.game.domain;

import com.krafton.api_server.api.auth.domain.User;
import jakarta.persistence.Entity;

@Entity
public class CatchLiar extends Game {
    private User liar;



}
