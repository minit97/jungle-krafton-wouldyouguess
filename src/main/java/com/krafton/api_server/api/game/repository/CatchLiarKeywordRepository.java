package com.krafton.api_server.api.game.repository;


import com.krafton.api_server.api.game.domain.CatchLiarKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CatchLiarKeywordRepository extends JpaRepository<CatchLiarKeyword, Long> {
    @Query(value = "SELECT * FROM catch_liar_keyword ORDER BY RAND() LIMIT 1", nativeQuery = true)
    CatchLiarKeyword findRandomCatchLiarKeyword();
}
