package com.krafton.api_server.api.auth.controller;

import com.krafton.api_server.api.auth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        return "Login successful";
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "Login failed";
    }
}