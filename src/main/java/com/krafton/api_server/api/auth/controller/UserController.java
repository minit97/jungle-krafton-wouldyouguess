package com.krafton.api_server.api.auth.controller;

import com.krafton.api_server.api.auth.dto.UserRequestDto;
import com.krafton.api_server.api.auth.dto.UserResponseDto;
import com.krafton.api_server.api.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PatchMapping("/api/users/{userId}/nickname")
    public ResponseEntity<UserResponseDto> callUpdateNickname(@PathVariable Long userId, @RequestBody UserRequestDto request) {
        UserResponseDto updatedUser = userService.updateNickname(userId, request);
        return ResponseEntity.ok(updatedUser);
    }
}
