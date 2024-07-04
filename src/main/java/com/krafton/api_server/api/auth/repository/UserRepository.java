package com.krafton.api_server.api.auth.repository;

import com.krafton.api_server.api.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
