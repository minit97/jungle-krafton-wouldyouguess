package com.krafton.api_server.api.room.controller;

import com.krafton.api_server.api.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.krafton.api_server.api.room.dto.RoomRequest.RoomCreateRequest;
import static com.krafton.api_server.api.room.dto.RoomRequest.RoomUser;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/room")
    public ResponseEntity<Long> callCreateRoom(@RequestBody RoomCreateRequest roomCreateRequest) {
        Long roomId = roomService.createRoom(roomCreateRequest);
        return ResponseEntity.ok(roomId);
    }

    @GetMapping("/room/{roomId}/users")
    public ResponseEntity<List<RoomUser>> callRoomUsers(@PathVariable Long roomId) {
        List<RoomUser> roomUsers = roomService.getRoomUsers(roomId);
        return ResponseEntity.ok(roomUsers);
    }

    @PostMapping("/room/{roomId}/join")
    public ResponseEntity<Long> callJoinRoom(@PathVariable Long roomId, @RequestBody RoomCreateRequest roomCreateRequest) {
        Long reponseRoomId = roomService.joinRoom(roomId, roomCreateRequest);
        return ResponseEntity.ok(reponseRoomId);
    }

    @PostMapping("/room/{roomId}/exit")
    public void callExitRoom(@PathVariable Long roomId, @RequestBody RoomCreateRequest roomCreateRequest) {
        roomService.exitRoom(roomId, roomCreateRequest);
    }


}
