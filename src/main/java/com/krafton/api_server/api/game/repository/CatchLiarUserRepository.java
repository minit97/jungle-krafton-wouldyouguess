package com.krafton.api_server.api.game.repository;

import com.krafton.api_server.api.game.domain.CatchLiarUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatchLiarUserRepository extends JpaRepository<CatchLiarUser, Long> {
}
