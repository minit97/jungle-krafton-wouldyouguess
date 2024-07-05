package com.krafton.api_server.api.room.service;

import com.krafton.api_server.api.auth.repository.UserRepository;
import com.krafton.api_server.api.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.krafton.api_server.api.room.dto.RoomRequest.RoomCreateRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public void createRoom(RoomCreateRequest roomCreateRequest) {

    }

    public void joinRoom() {

    }

    public void deleteRoom(Long roomId) {

    }

    public void exitRoom() {

    }
}
