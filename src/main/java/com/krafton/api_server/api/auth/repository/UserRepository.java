package com.krafton.api_server.api.auth.repository;

import com.krafton.api_server.api.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoId(String kakaoId);

    List<User> findAllByOrderByTotalPointDesc();
}
