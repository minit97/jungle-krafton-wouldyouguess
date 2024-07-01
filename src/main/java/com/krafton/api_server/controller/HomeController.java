package com.krafton.api_server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // 사용자가 로그인한 상태
            return "logged-in-home";
        } else {
            // 사용자가 로그인하지 않은 상태
            return "guest-home";
        }
    }
}