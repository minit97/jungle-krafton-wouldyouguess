package com.krafton.api_server.api.game2.repository;

import com.krafton.api_server.api.game2.domain.FindDiffGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FindDiffGameRepository extends JpaRepository<FindDiffGame, Long> {
}
