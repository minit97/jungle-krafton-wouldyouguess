package com.krafton.api_server.api.room.service;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.auth.repository.UserRepository;
import com.krafton.api_server.api.room.domain.Room;
import com.krafton.api_server.api.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.krafton.api_server.api.room.dto.RoomRequest.RoomCreateRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public Long createRoom(RoomCreateRequest roomCreateRequest) {
        User user = userRepository.findById(roomCreateRequest.getUserId())
                .orElseThrow(NoSuchElementException::new);
        Room room = Room.builder()
                .creater(user)
                .build();
        Room createdRoom = roomRepository.save(room);
        return createdRoom.getId();
    }

    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(NoSuchElementException::new);
        roomRepository.delete(room);
    }

    public void joinRoom(Long roomId, RoomCreateRequest roomCreateRequest) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(NoSuchElementException::new);
        User joinedUser = userRepository.findById(roomCreateRequest.getUserId())
                .orElseThrow(NoSuchElementException::new);
        room.joinRoom(joinedUser);
    }


    public void exitRoom(Long roomId, RoomCreateRequest roomCreateRequest) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(NoSuchElementException::new);
        User exitedUser = userRepository.findById(roomCreateRequest.getUserId())
                .orElseThrow(NoSuchElementException::new);

        if(room.getCreater().equals(exitedUser)) {
            deleteRoom(roomId);
        } else {
            room.exitRoom(exitedUser);
        }
    }
}
