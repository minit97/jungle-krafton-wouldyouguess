package com.krafton.api_server.api.game1.repository;

import com.krafton.api_server.api.game1.domain.CatchLiarGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatchLiarGameRepository extends JpaRepository<CatchLiarGame, Long> {
}
