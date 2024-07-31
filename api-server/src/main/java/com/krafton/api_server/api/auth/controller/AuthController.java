package com.krafton.api_server.api.auth.controller;

import com.krafton.api_server.api.auth.dto.UserResponseDto;
import com.krafton.api_server.api.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<UserResponseDto> kakaoLogin(@RequestParam("code") String code) {
        String kakaoAccessToken = authService.getKakaoAccessToken(code);
        UserResponseDto response = authService.kakaoLogin(kakaoAccessToken);
        return ResponseEntity.ok(response);
    }
}