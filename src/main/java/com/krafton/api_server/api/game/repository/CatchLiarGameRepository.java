package com.krafton.api_server.api.game.repository;

import com.krafton.api_server.api.game.domain.CatchLiarGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatchLiarGameRepository extends JpaRepository<CatchLiarGame, Long> {
}
