package com.krafton.api_server.api.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginApiController {

    @GetMapping("/login")
    public ResponseEntity<?> loginStatus(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        if (authentication != null && authentication.isAuthenticated()) {
            response.put("status", "authenticated");
            response.put("user", authentication.getName());
        } else {
            response.put("status", "not authenticated");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> responseBody = new HashMap<>();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            responseBody.put("status", "success");
            responseBody.put("message", "Logged out successfully");
        } else {
            responseBody.put("status", "failed");
            responseBody.put("message", "No active session found");
        }
        return ResponseEntity.ok(responseBody);
    }
}
