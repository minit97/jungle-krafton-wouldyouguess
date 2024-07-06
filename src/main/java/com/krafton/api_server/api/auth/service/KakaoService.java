package com.krafton.api_server.api.auth.service;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.auth.dto.CustomOAuth2User;
import com.krafton.api_server.api.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oauth2User.getAttributes();

        String kakaoId = attributes.get("id").toString();
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        String nickname = properties != null ? (String) properties.get("nickname") : null;
        String email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;

        log.info("Kakao ID: {}", kakaoId);
        log.info("Nickname: {}", nickname);
        log.info("Email: {}", email);

        // 사용자 정보를 처리하고 필요한 경우 DB에 저장
        User user = saveOrUpdateUser(kakaoId, nickname, email);

        // CustomOAuth2User 객체를 생성하여 반환
        return new CustomOAuth2User(oauth2User, user);
    }

    private User saveOrUpdateUser(String kakaoId, String nickname, String email) {
        return userRepository.findByKakaoId(kakaoId)
                .map(existingUser -> {
                    existingUser.update(kakaoId, email, nickname);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> userRepository.save(User.builder()
                        .kakaoId(kakaoId)
                        .email(email)
                        .nickname(nickname)
                        .build()));
    }
}