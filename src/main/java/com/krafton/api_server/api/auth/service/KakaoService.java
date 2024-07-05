package com.krafton.api_server.api.auth.service;

import com.krafton.api_server.api.auth.dto.KakaoResponse;
import com.krafton.api_server.api.auth.dto.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    private final String KAUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAUTH_USER_URL = "https://kapi.kakao.com/v2/user/me";

    private final RestTemplate restTemplate;

    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<KakaoResponse> responseEntity = restTemplate.postForEntity(KAUTH_TOKEN_URL, requestEntity, KakaoResponse.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            KakaoResponse kakaoResponse = responseEntity.getBody();
            if (kakaoResponse != null && kakaoResponse.getAccessToken() != null) {
                return kakaoResponse.getAccessToken();
            }
        }
        throw new RuntimeException("Failed to get Kakao access token");
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfo> responseEntity = restTemplate.exchange(
                KAUTH_USER_URL,
                HttpMethod.GET,
                requestEntity,
                KakaoUserInfo.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            KakaoUserInfo userInfo = responseEntity.getBody();
            if (userInfo != null) {
                log.info("[ Kakao Service ] Auth ID ---> {} ", userInfo.getId());
                log.info("[ Kakao Service ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getUsername());
                log.info("[ Kakao Service ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());
                return userInfo;
            }
        }
        throw new RuntimeException("Failed to get Kakao user info");
    }
}