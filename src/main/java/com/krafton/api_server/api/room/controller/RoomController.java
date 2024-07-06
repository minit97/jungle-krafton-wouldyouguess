package com.krafton.api_server.api.room.controller;

import com.krafton.api_server.api.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.krafton.api_server.api.room.dto.RoomRequest.RoomCreateRequest;

@Slf4j
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

    @DeleteMapping("/room/{roomId}")
    public void callDeleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
    }

    @PostMapping("/room/{roomId}/join")
    public void callJoinRoom(@PathVariable Long roomId, @RequestBody RoomCreateRequest roomCreateRequest) {
        roomService.joinRoom(roomId, roomCreateRequest);
    }

    @PostMapping("/room/{roomId}/exit")
    public void callExitRoom(@PathVariable Long roomId, @RequestBody RoomCreateRequest roomCreateRequest) {
        roomService.exitRoom(roomId, roomCreateRequest);
    }

}
