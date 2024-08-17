package com.krafton.api_server.api.game1.repository;

import com.krafton.api_server.api.game1.domain.CatchLiarUser;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CatchLiarUserRepository extends JpaRepository<CatchLiarUser, Long> {

    @Query("SELECT clu FROM CatchLiarUser clu WHERE clu.id = :id AND clu.catchLiarGame.id = :gameId")
    Optional<CatchLiarUser> findByIdAndCatchLiarGameId(@Param("id") Long id, @Param("gameId") Long gameId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT clu FROM CatchLiarUser clu WHERE clu.id = :id AND clu.catchLiarGame.id = :gameId")
    Optional<CatchLiarUser> findByIdAndCatchLiarGameIdPessimistic(@Param("id") Long id, @Param("gameId") Long gameId);

}
