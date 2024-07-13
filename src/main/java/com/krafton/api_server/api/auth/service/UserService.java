package com.krafton.api_server.api.auth.service;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.auth.dto.TempLogin;
import com.krafton.api_server.api.auth.dto.UserRequestDto;
import com.krafton.api_server.api.auth.dto.UserResponseDto;
import com.krafton.api_server.api.auth.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final HttpSession httpSession;
    private final UserRepository userRepository;

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


    @Transactional
    public UserResponseDto updateNickname(Long userId, UserRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(NoSuchElementException::new);
        user.updateNickname(request.getNickname());
        return UserResponseDto.from(user);
    }

    public UserResponseDto getTempLogin(TempLogin tempLogin) {
        User user = User.builder()
                .username(tempLogin.getUsername())
                .nickname(tempLogin.getNickname())
                .build();
        User saved = userRepository.save(user);
        return UserResponseDto.from(saved);
    }
}
