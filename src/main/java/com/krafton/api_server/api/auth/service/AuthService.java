package com.krafton.api_server.api.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.auth.dto.KakaoTokenDto;
import com.krafton.api_server.api.auth.dto.KakaoUserInfo;
import com.krafton.api_server.api.auth.dto.LoginResponseDto;
import com.krafton.api_server.api.auth.repository.UserRepository;
import com.krafton.api_server.api.auth.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;


    @Value("${kakao.token-uri}")
    private String KAKAO_TOKEN_URI;

    @Value("${kakao.user-info}")
    private String KAKAO_USER_URI;

    public String getKakaoAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("redirect_uri", KAKAO_REDIRECT_URI);
        params.add("code", code);
//        params.add("client_secret", KAKAO_CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                KAKAO_TOKEN_URI, // https://kauth.kakao.com/oauth/token
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON Parsing (KakaoTokenDto)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KakaoTokenDto kakaoTokenDto = null;

        try {
            kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoTokenDto.getAccess_token();
    }

    public KakaoUserInfo getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> kakaoUserInfoRequest = new HttpEntity<>(headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> userInfoResponse = rt.exchange(
                KAKAO_USER_URI,
                HttpMethod.GET,
                kakaoUserInfoRequest,
                String.class
        );
        String responseBody = userInfoResponse.getBody();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(responseBody, KakaoUserInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Kakao user info", e);
        }
    }

    public ResponseEntity<LoginResponseDto> kakaoLogin(String accessToken) {

        // 액세스 토큰으로 사용자 정보 요청
        KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 사용자 정보로 회원가입 또는 로그인 처리
        User user = userRepository.findByKakaoId(kakaoUserInfo.getId())
                .orElseGet(() -> registerUser(kakaoUserInfo));

        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.createAccessToken(user.getKakaoId());

        // JWT refresh 토큰 생성
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getKakaoId());

        // 로그인 응답 생성
        LoginResponseDto loginResponse = new LoginResponseDto(true, "Login successful", jwtToken, refreshToken, user.getUsername());

        return ResponseEntity.ok().body(loginResponse);
    }

    private User registerUser(KakaoUserInfo kakaoUserInfo) {
        User user = User.builder()
                .kakaoId(kakaoUserInfo.getId())
                .username(kakaoUserInfo.getKakaoAccount().getProfile().getNickname())
                .build();
        
        return userRepository.save(user);
    }

}
