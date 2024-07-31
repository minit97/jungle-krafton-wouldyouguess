package com.krafton.api_server.api.room.service;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.auth.repository.UserRepository;
import com.krafton.api_server.api.room.domain.Room;
import com.krafton.api_server.api.room.dto.request.RoomRequestDto;
import com.krafton.api_server.api.room.dto.response.RoomUserResponseDto;
import com.krafton.api_server.api.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public Long createRoom(RoomRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(NoSuchElementException::new);

        Room room = Room.builder()
                .user(user)
                .build();
        Room createdRoom = roomRepository.save(room);
        return createdRoom.getId();
    }

    @Transactional(readOnly = true)
    public List<RoomUserResponseDto> getRoomUsers(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(NoSuchElementException::new);

        List<User> participants = room.getParticipants();
        return participants.stream().map(RoomUserResponseDto::from).toList();
    }

    public Long joinRoom(Long roomId, RoomRequestDto request) {
        User joinedUser = userRepository.findById(request.getUserId()).orElseThrow(NoSuchElementException::new);

        Room room = roomRepository.findById(roomId).orElseThrow(NoSuchElementException::new);
        room.joinRoom(joinedUser);
        return room.getId();
    }

    public void exitRoom(Long roomId, RoomRequestDto request) {
        Room room = roomRepository.findById(roomId).orElseThrow(NoSuchElementException::new);
        User exitedUser = userRepository.findById(request.getUserId()).orElseThrow(NoSuchElementException::new);

        room.exitRoom(exitedUser);
        if (room.getParticipants().size() == 0) {
            roomRepository.delete(room);
        }
    }

}
