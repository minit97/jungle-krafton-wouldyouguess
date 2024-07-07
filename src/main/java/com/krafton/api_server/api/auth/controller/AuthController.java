package com.krafton.api_server.api.auth.controller;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.auth.dto.CustomOAuth2User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/loginSuccess")
    public ResponseEntity<?> loginSuccess(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, HttpSession session) {
        User user = customOAuth2User.getUser();
        session.setAttribute("user", user);
        return ResponseEntity.ok(Map.of("message", "Login successful", "user", user));
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "redirect:/login";
    }

    @GetMapping("/api/user")
    public ResponseEntity<?> getUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not logged in"));
        }
        return ResponseEntity.ok(user);
    }

}