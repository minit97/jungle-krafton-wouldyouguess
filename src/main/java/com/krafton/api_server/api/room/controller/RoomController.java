package com.krafton.api_server.api.room.controller;

import com.krafton.api_server.api.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.krafton.api_server.api.room.dto.RoomRequest.RoomCreateRequest;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/room")
    public void callCreateRoom(@RequestBody RoomCreateRequest roomCreateRequest) {
        roomService.createRoom(roomCreateRequest);
    }

    @DeleteMapping("/room/{roomId}")
    public void callDeleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
    }

    @PostMapping("/room/{roomId}/join")
    public void callJoinRoom(@RequestBody RoomCreateRequest roomCreateRequest) {
        roomService.joinRoom();
    }

    @PostMapping("/room/{roomId}/exit")
    public void callExitRoom(@RequestBody RoomCreateRequest roomCreateRequest) {
        roomService.exitRoom();
    }

}
