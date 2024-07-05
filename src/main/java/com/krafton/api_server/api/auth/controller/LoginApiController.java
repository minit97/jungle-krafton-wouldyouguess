package com.krafton.api_server.api.auth.controller;

import com.krafton.api_server.api.auth.dto.KakaoUserInfo;
import com.krafton.api_server.api.auth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginApiController {

    private final KakaoService kakaoService;
    @GetMapping("/login/oauth2/code/kakao")
    public String kakaoCallback(@RequestParam("code") String code) {
        try {
            String accessToken = kakaoService.getAccessToken(code);
            KakaoUserInfo userInfo = kakaoService.getUserInfo(accessToken);
            // accessToken을 사용하여 추가 작업 수행
            return "Access Token: " + accessToken + "User Info : " + userInfo;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}