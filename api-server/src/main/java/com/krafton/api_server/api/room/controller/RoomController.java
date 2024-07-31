package com.krafton.api_server.api.room.controller;

import com.krafton.api_server.api.room.dto.request.RoomRequestDto;
import com.krafton.api_server.api.room.dto.response.RoomUserResponseDto;
import com.krafton.api_server.api.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/room")
@RestController
public class RoomController {

    private final RoomService roomService;

    @PostMapping()
    public ResponseEntity<Long> callCreateRoom(@RequestBody RoomRequestDto request) {
        Long roomId = roomService.createRoom(request);
        return ResponseEntity.ok(roomId);
    }

    @GetMapping("/{roomId}/users")
    public ResponseEntity<List<RoomUserResponseDto>> callRoomUsers(@PathVariable Long roomId) {
        List<RoomUserResponseDto> response = roomService.getRoomUsers(roomId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<Long> callJoinRoom(@PathVariable Long roomId, @RequestBody RoomRequestDto request) {
        Long reponseRoomId = roomService.joinRoom(roomId, request);
        return ResponseEntity.ok(reponseRoomId);
    }

    @PostMapping("/{roomId}/exit")
    public void callExitRoom(@PathVariable Long roomId, @RequestBody RoomRequestDto request) {
        roomService.exitRoom(roomId, request);
    }


}
