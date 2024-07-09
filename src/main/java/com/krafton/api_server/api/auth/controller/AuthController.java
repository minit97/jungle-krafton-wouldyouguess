package com.krafton.api_server.api.auth.controller;

import com.krafton.api_server.api.auth.dto.KakaoTokenDto;
import com.krafton.api_server.api.auth.dto.LoginResponseDto;
import com.krafton.api_server.api.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @CrossOrigin(origins = "http://localhost:5173") // React 클라이언트 도메인에 대한 CORS 설정
    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestParam("code") String code) {
        try {
            String kakaoAccessToken = authService.getKakaoAccessToken(code);
            return authService.kakaoLogin(kakaoAccessToken);
        } catch (Exception e) {
            log.error("Kakao login failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDto(false, "Login failed"));
        }
    }


}