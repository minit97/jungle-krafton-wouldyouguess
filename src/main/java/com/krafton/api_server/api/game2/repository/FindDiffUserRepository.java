package com.krafton.api_server.api.game2.repository;

import com.krafton.api_server.api.game2.domain.FindDiffUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FindDiffUserRepository extends JpaRepository<FindDiffUser, Long> {
    Optional<FindDiffUser> findByUserId(Long userId);
}
