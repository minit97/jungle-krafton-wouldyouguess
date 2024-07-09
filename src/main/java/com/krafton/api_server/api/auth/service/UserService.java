package com.krafton.api_server.api.auth.service;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.auth.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final HttpSession httpSession;
    private final UserRepository userRepository;

    public UserService(HttpSession httpSession, UserRepository userRepository) {
        this.httpSession = httpSession;
        this.userRepository = userRepository;
    }

    public User getUser() {
        return (User) httpSession.getAttribute("user");
    }

    public void addPoint(Long point) {
        Long kakaoId = getUser().getKakaoId();
        Optional<User> userOptional = userRepository.findByKakaoId(kakaoId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.addPoint(point);
            userRepository.save(user);
            updateUserRanks();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void updateUserRanks() {
        List<User> users = userRepository.findAllByOrderByTotalPointDesc();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            user.setRank((long) (i + 1));
        }
    }


}
